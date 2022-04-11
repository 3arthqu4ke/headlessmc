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

    // TODO: can we improve this and not create a new ClassLoader everytime?
    private static final class EntryClassLoader extends URLClassLoader {
        public EntryClassLoader(URL[] urls) {
            super(urls, EntryClassLoader.class.getClassLoader());
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

}
