package me.earth.headlessmc.modlauncher;

import dev.xdark.deencapsulation.Deencapsulation;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * cpw's BootstrapLauncher turns a classpath environment into a modularized one.
 * But this requires that the BootstrapLauncher and all libraries are loaded from the boot layer.
 * When launching in memory we cannot launch from boot layer because we do not know which version to launch.
 * This launcher introduces a layer between boot layer and MC-BOOTSTRAP layer,
 * which creates and contains the modules from the ignoreList,
 * which cpw's bootstraplauncher would usually leave in the boot layer.
 * @see <a href="https://github.com/McModLauncher/bootstraplauncher">https://github.com/McModLauncher/bootstraplauncher</a>
 */
@SuppressWarnings({"unused", "Since15"}) // called via Reflection from me.earth.headlessmc.launcher.launch.InMemoryLauncher
public class LayeredBootstrapLauncher {
    private static final Logger LOGGER = Logger.getLogger(LayeredBootstrapLauncher.class.getSimpleName());

    private final List<Path> classpath;
    private final URL[] classpathUrls;
    private final String mainClass;

    /**
     * Constructs a new LayeredBootstrapLauncher.
     *
     * @param classpath the classpath to load.
     * @param classpathUrls the classpath, just mapped to URLs.
     * @param mainClass the name of the mainClass to call.
     */
    public LayeredBootstrapLauncher(List<Path> classpath, URL[] classpathUrls, String mainClass) {
        this.classpath = classpath;
        this.classpathUrls = classpathUrls;
        this.mainClass = mainClass;
    }

    /**
     * Launches the game on the with the configured classPath and mainClass for the given arguments.
     *
     * @param args the arguments to launch the game with.
     * @throws IOException if something goes wrong.
     */
    public void launch(String[] args) throws IOException {
        try (ModuleURLClassLoader classLoader = new ModuleURLClassLoader(classpathUrls, ClassLoader.getSystemClassLoader())) {
            try {
                String ignoreList = System.getProperty("ignoreList", "asm,securejarhandler");
                String[] ignores = ignoreList.split(",");
                Set<Path> validPaths = findValidPaths(ignores);
                LOGGER.fine("Valid paths: " + validPaths);

                ModuleFinder finder = ModuleFinder.of(validPaths.toArray(new Path[0]));
                Set<String> moduleNames = new HashSet<>();
                finder.findAll().forEach(moduleReference -> moduleNames.add(moduleReference.descriptor().name()));
                LOGGER.fine("Modules in paths: " + moduleNames);

                ModuleLayer bootLayer = ModuleLayer.boot(); // should be LayeredBootstrapLauncher.class.getModule.getLayer()?
                Configuration configuration = bootLayer.configuration().resolve(finder, ModuleFinder.of(), moduleNames);
                List<ModuleLayer> parents = new ArrayList<>(1);
                parents.add(bootLayer);

                ModuleLayer newLayer = ModuleLayer.defineModules(configuration, parents, ignored -> classLoader).layer();
                LOGGER.fine("Binding Classloader");
                ModuleURLClassLoader.bindToLayer(classLoader, newLayer);
                classLoader.setConfiguration(configuration);
                Thread.currentThread().setContextClassLoader(classLoader);

                LOGGER.fine("Hacking in ForgeFileSystemProviders");
                hackInForgeFileSystemProviders(classLoader);

                LOGGER.fine("Looking for main class " + mainClass);
                String mostPromisingModule = moduleNames.stream().filter(mainClass::startsWith).findFirst().orElse(null);
                Class<?> mainClassClass;
                if (mostPromisingModule == null) {
                    LOGGER.info("Looking for main class " + mainClass + " via findClass");
                    mainClassClass = classLoader.findClass(mainClass);
                } else {
                    LOGGER.fine("Looking for main class " + mainClass + " in module " + mostPromisingModule);
                    mainClassClass = classLoader.findClass(mostPromisingModule, mainClass);
                }

                if (mainClassClass == null) {
                    throw new RuntimeException("Failed to find main class " + mainClass);
                }

                Method main = mainClassClass.getMethod("main", String[].class);
                main.invoke(null, (Object) args);
            } catch (Exception e) {
                throw new RuntimeException("Failed to launch game", e);
            }
        }
    }

    private Set<Path> findValidPaths(String... ignores) {
        Set<Path> validPaths = new HashSet<>(ignores.length * 2);
        for (Path path : classpath) {
            boolean isIgnored = false;
            String fileName = path.getFileName().toString();
            for (String ignore : ignores) {
                if (fileName.startsWith(ignore)) {
                    isIgnored = true;
                    break;
                }
            }

            // We want the opposite of the BootstrapLauncher,
            // we want the Modules it usually ignores and build a layer from them.
            if (!isIgnored) {
                continue;
            }

            // this is kinda ugly and we should check if we can remove this
            // i noticed that ModuleFinders cannot find modules for some jars
            // so we verify first which jars actually contain modules
            ModuleFinder finder = ModuleFinder.of(path);
            try {
                finder.findAll(); // will throw an exception if it cant find a module
                validPaths.add(path);
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "Failed to find module for path " + path, e);
            }
        }

        return validPaths;
    }

    /**
     * A terrible hack!
     * Forge provides some own {@link java.nio.file.spi.FileSystemProvider}s.
     * These providers usually get loaded from the BootLayer on the SystemClassloader.
     * But when launching from memory we do not have these FileSystemProviders yet,
     * so we need to reload all FileSystemProvider from our Classloader and add them.
     *
     * @param classLoader the classloader to load the FileSystemProviders from.
     */
    private void hackInForgeFileSystemProviders(ClassLoader classLoader) {
        try {
            Deencapsulation.deencapsulate(FileSystemProvider.class);
            Field installedProviders = FileSystemProvider.class.getDeclaredField("installedProviders");
            installedProviders.setAccessible(true);

            List<FileSystemProvider> providers = new ArrayList<>();
            ServiceLoader.load(FileSystemProvider.class, classLoader).forEach(provider -> {
                LOGGER.fine("Found FileSystemProvider: " + provider);
                providers.add(provider);
            });

            installedProviders.set(null, providers);
        } catch (Throwable throwable) {
            LOGGER.log(Level.SEVERE, "Failed to hack in Forge FileSystemProviders", throwable);
        }
    }

}