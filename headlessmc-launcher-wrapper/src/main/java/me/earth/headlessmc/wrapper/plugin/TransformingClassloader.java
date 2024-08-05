package me.earth.headlessmc.wrapper.plugin;

import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

@Getter
public class TransformingClassloader extends URLClassLoader {
    private final URLClassLoader transformerClassloader;
    private final List<TransformerPlugin> plugins;

    public TransformingClassloader(URL[] urls, ClassLoader parent, URLClassLoader transformerClassloader, List<TransformerPlugin> plugins) {
        super(urls, parent);
        this.transformerClassloader = transformerClassloader;
        this.plugins = plugins;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // TODO: shouldnt this look up the parent classloader to?
        //  i am very suspicious of this!
        String path = name.replace('.', '/').concat(".class");
        try (InputStream is = this.getResourceAsStream(path)) {
            if (is == null) {
                throw new ClassNotFoundException(name);
            }

            byte[] classBytes = toByteArray(is);
            for (TransformerPlugin transformer : plugins) {
                try {
                    classBytes = transformer.getTransformer().transform(name, classBytes);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ClassNotFoundException(transformer.getName() + " failed to transform class " + name, e);
                }
            }

            return this.defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            transformerClassloader.close();
        } finally {
            super.close();
        }
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }

        return baos.toByteArray();
    }

}
