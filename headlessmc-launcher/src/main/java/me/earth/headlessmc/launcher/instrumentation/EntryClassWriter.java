package me.earth.headlessmc.launcher.instrumentation;

import lombok.CustomLog;
import lombok.Getter;
import lombok.val;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

@Getter
@CustomLog
public class EntryClassWriter extends ClassWriter implements AutoCloseable {
    private final ClassLoader classLoader;

    public EntryClassWriter(EntryStream stream) throws IOException {
        this(EntryClassLoader.from(stream));
    }

    public EntryClassWriter(ClassLoader classLoader) {
        super(COMPUTE_FRAMES);
        this.classLoader = classLoader;
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        // TODO: we are lucky that this has not bitten us yet,
        //  but loading the classes into this jvm is a bit problematic
        //  - graalvm wont work
        //  - what if we need to instrument something java 21 while headlessmc runs in java 17?
        //  - i do not think the static initializer runs, but still, we are loading foreign code
        try {
            return super.getCommonSuperClass(type1, type2);
        } catch (TypeNotPresentException | NoClassDefFoundError e) {
            log.error("Couldn't find common super class! " + type1 + ", " + type2 + " : " + e.getMessage());
            return "java/lang/Object";
        }
    }

    @Override
    public void close() throws IOException {
        if (classLoader instanceof AutoCloseable) {
            try {
                ((AutoCloseable) classLoader).close();
            } catch (IOException e) {
                throw e;
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

    @CustomLog
    private static final class EntryClassLoader extends URLClassLoader {
        public EntryClassLoader(URL[] urls) {
            super(urls, EntryClassLoader.class.getClassLoader());
        }

        @Override
        protected Class<?> findClass(String name)
            throws ClassNotFoundException {
            try {
                return super.findClass(name);
            } catch (ClassNotFoundException e) {
                log.debug(e.getMessage());
                return getParent().loadClass(name);
            }
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
            try {
                return super.loadClass(name, resolve);
            } catch (ClassNotFoundException e) {
                log.debug(e.getMessage());
                return getParent().loadClass(name);
            }
        }

        public static EntryClassLoader from(EntryStream stream) throws IOException {
            val urls = new ArrayList<URL>();
            for (val target : stream.getTargets()) {
                urls.add(new File(target.getPath()).toURI().toURL());
            }

            // TODO: this might not include the entire Minecraft classpath?
            //  if its just the targets of the stream
            //  this could become a problem if a class we instrument
            //  references a super-type from another jar?
            return new EntryClassLoader(urls.toArray(new URL[0]));
        }
    }

}
