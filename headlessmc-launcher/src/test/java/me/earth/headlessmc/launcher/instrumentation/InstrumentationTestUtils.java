package me.earth.headlessmc.launcher.instrumentation;

import lombok.SneakyThrows;
import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.jar.JarEntry;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InstrumentationTestUtils {
    @SneakyThrows
    public static Class<?> instrument(Class<?> clazz, Transformer transformer) {
        String path = clazz.getName().replace(".", "/").concat(".class");
        try (val is = InstrumentationTestUtils.class.getClassLoader()
                                                    .getResourceAsStream(path)) {
            assertNotNull(is);
            JarEntry entry = new JarEntry(path);
            val es = new EntryStream(is, emptyList(), entry::getName);
            try (val clIs = transformer.transform(es).getStream()) {
                return new ClassStreamLoader().define(clazz.getName(), clIs);
            }
        }
    }

    private static final class ClassStreamLoader extends ClassLoader {
        @SneakyThrows
        public Class<?> define(String name, InputStream is) {
            try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                int nRead;
                byte[] data = new byte[16384];

                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                val classBytes = buffer.toByteArray();
                return this.defineClass(name, classBytes, 0, classBytes.length);
            }
        }
    }

}
