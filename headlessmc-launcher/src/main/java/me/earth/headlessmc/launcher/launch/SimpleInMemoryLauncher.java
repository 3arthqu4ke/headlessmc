package me.earth.headlessmc.launcher.launch;

import lombok.CustomLog;
import lombok.Setter;
import me.earth.headlessmc.launcher.instrumentation.InstrumentationClassloader;
import me.earth.headlessmc.launcher.instrumentation.debug.DebugTransformer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.jar.JarFile;

/**
 * A simple launcher for java programs that loads the classpath onto an {@link URLClassLoader}.
 */
@Setter
@CustomLog
public class SimpleInMemoryLauncher {
    private Function<URL[], URLClassLoader> classLoaderFactory = urls -> {
        InstrumentationClassloader icl = new InstrumentationClassloader(urls, SimpleInMemoryLauncher.class.getClassLoader(), new ArrayList<>());
        icl.getTransformer().getTransformers().add(new DebugTransformer());
        return icl;
    };

    public void simpleLaunch(URL[] classpathUrls, String mainClass, List<String> gameArgs) throws LaunchException {
        ClassLoader classLoaderBefore = Thread.currentThread().getContextClassLoader();
        //try (URLClassLoader urlClassLoader = new URLClassLoader(classpathUrls)) {
        try (URLClassLoader urlClassLoader = classLoaderFactory.apply(classpathUrls)) {
            Thread.currentThread().setContextClassLoader(urlClassLoader);
            Class<?> mainClassClass = Class.forName(mainClass, false, urlClassLoader);
            Method main = mainClassClass.getDeclaredMethod("main", String[].class);
            main.setAccessible(true);
            main.invoke(null, (Object) gameArgs.toArray(new String[0]));
        } catch (InvocationTargetException e) {
            log.error(e);
            // not throwing????????????????????????
        } catch (Exception e) {
            throw new LaunchException("Failed to launch", e);
        } finally {
            Thread.currentThread().setContextClassLoader(classLoaderBefore);
        }
    }

    public String getMainClassFromJar(File file) throws IOException {
        try (JarFile jarFile = new JarFile(file)) {
            return Objects.requireNonNull(jarFile.getManifest().getMainAttributes().getValue("Main-Class"));
        }
    }

}
