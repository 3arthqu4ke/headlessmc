package me.earth.headlessmc.wrapper;

import me.earth.headlessmc.wrapper.plugin.TransformingClassloader;
import me.earth.headlessmc.wrapper.plugin.TransformingPluginFinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        Path root = Paths.get("HeadlessMC");
        Files.createDirectories(root);
        // TODO: save file with hash in name, then check if it already exists!
        Path jarPath = root.resolve("headlessmc-launcher.jar");

        try (InputStream is = Main.class.getClassLoader().getResourceAsStream("headlessmc/headlessmc-launcher.jar");
             OutputStream fos = Files.newOutputStream(jarPath)) {
            copy(Objects.requireNonNull(is, "Failed to find resource headlessmc/headlessmc-launcher.jar"), fos);
        }

        TransformingPluginFinder pluginFinder = new TransformingPluginFinder(root.resolve("transformers"));
        try (TransformingClassloader classloader = pluginFinder.build(jarPath, root.resolve("plugins"))) {
            HeadlessMcWrapper.setClassLoader(classloader);
            Thread.currentThread().setContextClassLoader(classloader);

            Class<?> mainClass = Class.forName("me.earth.headlessmc.launcher.Main", true, classloader);
            Method main = mainClass.getMethod("main", String[].class);
            main.invoke(null, (Object) args);
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
