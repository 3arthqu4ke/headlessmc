package me.earth.headlessmc.launcher.launch;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.Main;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.os.OS;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@CustomLog
@RequiredArgsConstructor
public class InMemoryLauncher {
    private final FileManager files;
    private final HasConfig config;
    private final OS os;
    private final LaunchOptions options;
    private final Command command;

    public void launch() throws IOException, LaunchException, AuthException {
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
                String[] split = arg.split("-D|=");
                if (split.length > 2) {
                    log.info("SystemProperty: " + split[1] + " : " + split[2]);
                    System.setProperty(split[1], split[2]);
                }
            }

            if (hasPassedMainClass) {
                gameArgs.add(arg);
            }

            if (arg.equals(mainClass)) {
                hasPassedMainClass = true;
            }
        }

        try (URLClassLoader urlClassLoader = new URLClassLoader(classpathUrls)) {
            try {
                Class<?> mainClassClass = Class.forName(mainClass, false, urlClassLoader);
                Method main = mainClassClass.getDeclaredMethod("main", String[].class);
                main.setAccessible(true);
                main.invoke(null, (Object) gameArgs.toArray(new String[0]));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (Exception e) {
                throw new LaunchException("Failed to launch game", e);
            }
        }
    }

}
