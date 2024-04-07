package me.earth.headlessmc.launcher.java;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

// TODO: This!
@CustomLog
@RequiredArgsConstructor
public class JavaService extends Service<Java> {
    private final JavaVersionParser parser = new JavaVersionParser();
    private final HasConfig cfg;

    @Override
    protected List<Java> update() {
        val systemDefaultJavaHome = Optional.ofNullable(System.getenv("JAVA_HOME"));
        val currentJavaHome = Optional.ofNullable(System.getProperty("java.home"));
        val foundJavaHomes = Stream.of(systemDefaultJavaHome, currentJavaHome)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(javaHome -> javaHome + "/bin/java")
                .toArray(String[]::new);
        val array = cfg.getConfig().get(LauncherProperties.JAVA, foundJavaHomes);
        val newVersions = new ArrayList<Java>(array.length);
        for (val path : array) {
            log.debug("Reading Java version at path: " + path);
            if (path.trim().isEmpty()) {
                continue;
            }

            try {
                val majorVersion = parser.parseVersionCommand(path);
                val java = new Java(path, majorVersion);
                log.debug("Found Java: " + java);
                newVersions.add(java);
            } catch (IOException e) {
                log.warning("Couldn't parse Java Version for path " + path);
                e.printStackTrace();
            }
        }

        return newVersions;
    }

    public Java findBestVersion(Integer version) {
        if (version == null) {
            log.error("Version was null, assuming Java 8 is needed!");
            return findBestVersion(8);
        }

        Java best = null;
        for (Java java : this) {
            if (version == java.getVersion()) {
                return java;
            }

            // uhhhhhhhhhhhhhhhhhhhh what?!?!?!
            if (java.getVersion() > version && (best == null
                || best.getVersion() - version > java.getVersion() - version)) {
                best = java;
            }
        }

        if (best == null) {
            log.error("Couldn't find a Java Version >= " + version + "!");
        } else {
            log.warning("Couldn't find Java Version " + version
                            + " falling back to " + best.getVersion());
        }

        return best;
    }

}
