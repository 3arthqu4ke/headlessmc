package me.earth.headlessmc.launcher.launch;

import lombok.CustomLog;
import me.earth.headlessmc.launcher.instrumentation.InstrumentationClassloader;
import me.earth.headlessmc.launcher.instrumentation.debug.DebugTransformer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple launcher for java programs that loads the classpath onto an {@link URLClassLoader}.
 */
@CustomLog
public class SimpleInMemoryLauncher {
    public void simpleLaunch(URL[] classpathUrls, String mainClass, String... args) throws IOException, LaunchException {
        this.simpleLaunch(classpathUrls, mainClass, Arrays.stream(args).collect(Collectors.toList()));
    }

    public void simpleLaunch(URL[] classpathUrls, String mainClass, List<String> gameArgs) throws IOException, LaunchException {
        ClassLoader classLoaderBefore = Thread.currentThread().getContextClassLoader();
        try (URLClassLoader urlClassLoader = new URLClassLoader(classpathUrls)) {
        //try (InstrumentationClassloader urlClassLoader = new InstrumentationClassloader(classpathUrls, SimpleInMemoryLauncher.class.getClassLoader(), new ArrayList<>())) {
        //    urlClassLoader.getTransformer().getTransformers().add(new DebugTransformer());
            try {
                Thread.currentThread().setContextClassLoader(urlClassLoader);
                Class<?> mainClassClass = Class.forName(mainClass, false, urlClassLoader);
                Method main = mainClassClass.getDeclaredMethod("main", String[].class);
                main.setAccessible(true);
                main.invoke(null, (Object) gameArgs.toArray(new String[0]));
            } catch (InvocationTargetException e) {
                log.error(e);
            } catch (Exception e) {
                throw new LaunchException("Failed to launch", e);
            } finally {
                Thread.currentThread().setContextClassLoader(classLoaderBefore);
            }
        }
    }

}
