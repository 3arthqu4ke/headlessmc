package me.earth.headlessmc.wrapper;

import me.earth.headlessmc.wrapper.plugin.TransformingClassloader;
import me.earth.headlessmc.wrapper.plugin.TransformingPluginFinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static final String CLOSE_CLASSLOADER_PROPERTY = "hmc.wrapper.close.classloader";
    public static final String WRAPPED_MAIN_PROPERTY = "hmc.wrapper.wrapped.main";
    public static final String DEFAULT_MAIN = "me.earth.headlessmc.launcher.Main";

    public static void main(String[] args) throws Exception {
        Path root = createRootDirectory();

        TransformingClassloader classloader = null;
        try {
            TransformingPluginFinder pluginFinder = HeadlessMcWrapper.getPluginFinderFactory().apply(root.resolve("transformers"));
            URL url = Main.class.getClassLoader().getResource("headlessmc/headlessmc-launcher.jar");
            System.out.println("HeadlessMC Launcher Jar: " + url);
            classloader = pluginFinder.build(url, root.resolve("plugins"));
            HeadlessMcWrapper.setClassLoader(classloader);
            Thread.currentThread().setContextClassLoader(classloader);

            Class<?> mainClass = Class.forName(System.getProperty(WRAPPED_MAIN_PROPERTY, DEFAULT_MAIN), true, classloader);
            Method main = mainClass.getMethod("main", String[].class);
            main.invoke(null, (Object) args);
        } finally {
            if (classloader != null && Boolean.parseBoolean(System.getProperty(CLOSE_CLASSLOADER_PROPERTY, "true"))) {
                classloader.close();
            }
        }
    }

    public static Path createRootDirectory() throws IOException {
        Path root = Paths.get("HeadlessMC");
        Files.createDirectories(root);
        return root;
    }

    public static void extractResource(String resource, Path jarPath) throws IOException {
        try (InputStream is = Main.class.getClassLoader().getResourceAsStream(resource);
             OutputStream fos = Files.newOutputStream(jarPath)) {
            copy(Objects.requireNonNull(is, "Failed to find resource " + resource), fos);
        }
    }

    private static void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

}
