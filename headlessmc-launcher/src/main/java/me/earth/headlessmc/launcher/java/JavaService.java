package me.earth.headlessmc.launcher.java;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.Service;
import me.earth.headlessmc.launcher.util.PathUtil;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.LinkedHashSet;
import java.util.Set;

// TODO: This!
@CustomLog
@RequiredArgsConstructor
public class JavaService extends Service<Java> {
    private final JavaVersionParser parser = new JavaVersionParser();
    private final HasConfig cfg;
    private Java current;

    @Override
    protected Set<Java> update() {
        String[] array = cfg.getConfig().get(LauncherProperties.JAVA, new String[0]);
        Set<Java> newVersions = new LinkedHashSet<Java>((int) (array.length * 1.5));
        for (String path : array) {
            Java java = scanJava(path);
            if (java != null) {
                newVersions.add(java);
            }
        }

        if (System.getenv("JAVA_HOME") != null) {
            try {
                Java java = scanJava(PathUtil.stripQuotes(System.getenv("JAVA_HOME")).resolve("bin").resolve("java").toAbsolutePath().toString());
                if (java != null) {
                    newVersions.add(java);
                }
            } catch (InvalidPathException e) {
                log.error(e);
            }
        }

        newVersions.add(getCurrent());
        return newVersions;
    }

    public @Nullable Java scanJava(String path) {
        log.debug("Reading Java version at path: " + path);
        if (path.trim().isEmpty()) {
            return null;
        }

        try {
            int majorVersion = parser.parseVersionCommand(path);
            Java java = new Java(path.replace("\\", "/"), majorVersion);
            log.debug("Found Java: " + java);
            return java;
        } catch (IOException e) {
            log.warn("Couldn't parse Java Version for path " + path, e);
        }

        return null;
    }

    public @Nullable Java findBestVersion(Integer version) {
        if (version == null) {
            log.error("Version was null, assuming Java 8 is needed!");
            return findBestVersion(8);
        }

        Java best = null;
        for (Java java : this) {
            if ("current".equals(java.getPath())) {
                continue;
            }

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

    public Java getCurrent() {
        if (current == null) {
            String executable = PathUtil.stripQuotesAtStartAndEnd(System.getProperty("java.home", "current")).replace("\"", "").concat("/bin/java");
            String version = System.getProperty("java.version");
            if (version == null) {
                if ("current".equals(executable)) {
                    throw new IllegalStateException("Failed to parse current Java version!");
                }

                current = scanJava(executable);
            } else {
                current = new Java(executable, parseSystemProperty(version));
            }
        }

        return current;
    }

    @VisibleForTesting
    int parseSystemProperty(String versionIn) {
        String version = versionIn;
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }

        return Integer.parseInt(version);
    }

}
