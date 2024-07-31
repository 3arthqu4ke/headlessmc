package me.earth.headlessmc.lwjgl.launchwrapper;

import lombok.CustomLog;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

@CustomLog
public class LaunchWrapperClassloader extends URLClassLoader {
    public LaunchWrapperClassloader() {
        super(setupUrls(), ClassLoader.getSystemClassLoader());
    }

    private static URL[] setupUrls() {
        String[] cp = System.getProperty("java.class.path").split(
            System.getProperty("path.separator", File.pathSeparator));

        List<URL> urls = new ArrayList<>(cp.length);
        for (String path : cp) {
            File file = new File(path);
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException malformedURLException) {
                try {
                    urls.add(new URL(path));
                } catch (MalformedURLException e) {
                    log.error(e);
                }
            }
        }

        return urls.toArray(new URL[0]);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.loadClass(name, false);
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve)
        throws ClassNotFoundException {
        Class<?> clazz = findClass(name);
        if (resolve) {
            resolveClass(clazz);
        }

        return clazz;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return getParent().loadClass(name);
    }

}
