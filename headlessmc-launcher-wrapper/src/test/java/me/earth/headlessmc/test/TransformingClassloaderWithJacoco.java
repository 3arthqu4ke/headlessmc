package me.earth.headlessmc.test;

import me.earth.headlessmc.wrapper.plugin.TransformerPlugin;
import me.earth.headlessmc.wrapper.plugin.TransformingClassloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class TransformingClassloaderWithJacoco extends TransformingClassloader {
    public TransformingClassloaderWithJacoco(URL[] urls, ClassLoader parent, URLClassLoader transformerClassloader, List<TransformerPlugin> plugins) {
        super(urls, parent, transformerClassloader, plugins);
    }

    @Override
    protected byte[] instrument(String name, byte[] classBytes) throws ClassNotFoundException {
        byte[] result = super.instrument(name, classBytes);
        return result;
    }

}
