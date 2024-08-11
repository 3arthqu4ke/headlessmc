package me.earth.headlessmc.api.classloading;

import lombok.CustomLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

@CustomLog
public class IsolatedClassLoader extends URLClassLoader {
    public IsolatedClassLoader() {
        super(new URL[0], ClassLoader.getSystemClassLoader());
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return findClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.startsWith("me.earth.")) {
            try {
                String path = name.replace('.', '/').concat(".class");
                try (InputStream inputStream = getParent().getResourceAsStream(path)) {
                    if (inputStream == null) {
                        throw new ClassNotFoundException(name);
                    }

                    byte[] bytecode = readAllBytes(inputStream);
                    return defineClass(name, bytecode, 0, bytecode.length);
                }
            } catch (IOException e) {
                log.error("Failed to load class " + name, e);
            }
        }

        return getParent().loadClass(name);
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }

}
