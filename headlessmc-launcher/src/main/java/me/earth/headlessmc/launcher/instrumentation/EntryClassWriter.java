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

@CustomLog
public class EntryClassWriter extends ClassWriter {
    @Getter
    private final ClassLoader classLoader;

    public EntryClassWriter(EntryStream stream) throws IOException {
        super(COMPUTE_FRAMES);
        this.classLoader = EntryClassLoader.from(stream);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        try {
            return super.getCommonSuperClass(type1, type2);
        } catch (TypeNotPresentException | NoClassDefFoundError e) {
            log.error("Couldn't find common super class! " + type1 + ", "
                          + type2 + " : " + e.getMessage());
            return "java/lang/Object";
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

        public static EntryClassLoader from(EntryStream stream)
            throws IOException {
            val urls = new ArrayList<URL>();
            for (val target : stream.getTargets()) {
                urls.add(new File(target.getPath()).toURI().toURL());
            }

            return new EntryClassLoader(urls.toArray(new URL[0]));
        }
    }

}
