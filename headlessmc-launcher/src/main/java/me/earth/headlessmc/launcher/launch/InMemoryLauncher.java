package me.earth.headlessmc.launcher.launch;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.util.PathUtil;
import me.earth.headlessmc.launcher.version.Version;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// TOP 10 worst ideas #9 this
@CustomLog
@RequiredArgsConstructor
public class InMemoryLauncher {
    private final LaunchOptions options;
    private final Command command;
    private final Version version;
    private final Java java;

    public void launch() throws IOException, LaunchException, AuthException {
        log.warning("The In-Memory Launcher is a BETA feature and has not been thoroughly tested yet!");
        if (options.isForceBoot() && options.isForceSimple()) {
            throw new LaunchException("Both -forceSimple and -forceBoot specified!");
        }

        String mainClass = command.getActualMainClass(new ArrayList<>());
        URL[] classpathUrls = new URL[command.getClasspath().size()];
        for (int i = 0; i < command.getClasspath().size(); i++) {
            log.info(command.getClasspath().get(i));
            classpathUrls[i] = Paths.get(command.getClasspath().get(i)).toUri().toURL();
        }

        List<String> actualCommand = command.build();
        List<String> gameArgs = new ArrayList<>();
        boolean hasPassedMainClass = false;
        for (String arg : actualCommand) {
            if (arg.startsWith("-D")) {
                // TODO: not very good, split at first!
                String[] split = arg.split("-D|=");
                if (split.length > 2) {
                    log.info("SystemProperty: " + split[1] + " : " + split[2]);
                    if ("java.library.path".equals(split[1])) {
                        for (String library : split[2].split(File.pathSeparator)) {
                            addLibraryPath(PathUtil.stripQuotes(library));
                        }
                    } else {
                        System.setProperty(split[1], split[2]);
                    }
                } else {
                    System.setProperty(split[1], "");
                }
            }

            if (hasPassedMainClass) {
                gameArgs.add(arg);
            }

            if (arg.equals(mainClass)) {
                hasPassedMainClass = true;
            }
        }

        System.setProperty("legacyClassPath", String.join(File.pathSeparator, command.getClasspath()));
        System.setProperty("java.class.path", String.join(File.pathSeparator, command.getClasspath()));

        if (java.getVersion() > 8
            && !options.isForceSimple()
            && ("cpw.mods.bootstraplauncher.BootstrapLauncher".equals(version.getMainClass()) || options.isForceBoot())) {
            log.info("Launching with Java-9 in-memory launcher");
            java9Launch(classpathUrls, mainClass, gameArgs);
        } else {
            log.info("Launching with simple in-memory launcher.");
            simpleLaunch(classpathUrls, mainClass, gameArgs);
        }
    }

    private void simpleLaunch(URL[] classpathUrls, String mainClass, List<String> gameArgs) throws IOException, LaunchException {
        try (URLClassLoader urlClassLoader = new URLClassLoader(classpathUrls)) {
            try {
                Thread.currentThread().setContextClassLoader(urlClassLoader);
                Class<?> mainClassClass = Class.forName(mainClass, false, urlClassLoader);
                Method main = mainClassClass.getDeclaredMethod("main", String[].class);
                main.setAccessible(true);
                main.invoke(null, (Object) gameArgs.toArray(new String[0]));
            } catch (InvocationTargetException e) {
                log.error(e);
            } catch (Exception e) {
                throw new LaunchException("Failed to launch game", e);
            }
        }
    }

    private void java9Launch(URL[] classpathUrls, String mainClass, List<String> gameArgs) {
        try {
            Class<?> bootstrapLauncherClass = Class.forName("me.earth.headlessmc.modlauncher.LayeredBootstrapLauncher");
            Constructor<?> constructor = bootstrapLauncherClass.getConstructor(List.class, URL[].class, String.class);
            Object bootstrapLauncher = constructor.newInstance(command.getClasspath().stream().map(Paths::get).collect(Collectors.toList()), classpathUrls, mainClass);
            Method launch = bootstrapLauncherClass.getMethod("launch", String[].class);
            launch.invoke(bootstrapLauncher, (Object) gameArgs.toArray(new String[0]));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"JavaReflectionMemberAccess", "RedundantSuppression"})
    private void addLibraryPath(Path libraryPath) {
        try {
            // https://stackoverflow.com/questions/15409223/adding-new-paths-for-native-libraries-at-runtime-in-java
            if (java.getVersion() <= 8) {
                String libraryPathToAdd = libraryPath.toString();

                Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
                usrPathsField.setAccessible(true);

                String[] paths = (String[]) usrPathsField.get(null);
                for (String path : paths) {
                    if (path.equals(libraryPathToAdd)) {
                        return;
                    }
                }

                String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
                newPaths[newPaths.length - 1] = libraryPathToAdd;
                usrPathsField.set(null, newPaths);
            }
        } catch (Exception e) {
            log.error("Failed to add " + libraryPath + " to library path", e);
        }
    }

}
