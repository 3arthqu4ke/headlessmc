package me.earth.headlessmc.launcher.java;

import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.java.Java;
import me.earth.headlessmc.java.JavaScanner;
import me.earth.headlessmc.java.JavaVersionFinder;
import me.earth.headlessmc.java.JavaVersionParser;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.LazyService;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.util.PathUtil;
import me.earth.headlessmc.os.OS;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@CustomLog
@RequiredArgsConstructor
public class JavaService extends LazyService<Java> implements JavaScanner {
    @Getter
    private final JavaVersionParser parser = new JavaVersionParser();
    private final ConfigService cfg;
    private final OS os;

    private Java current;

    @Override
    protected Set<Java> update() {
        long nanos = System.nanoTime();
        String[] array = cfg.getConfig().get(LauncherProperties.JAVA, new String[0]);
        Set<Java> newVersions = new LinkedHashSet<>((int) (array.length * 1.5));
        for (String path : array) {
            Java java = scanJava(path);
            if (java != null) {
                newVersions.add(java);
            }
        }

        JavaScanner javaScanner = JavaScanner.of(parser);
        JavaVersionFinder javaVersionFinder = new JavaVersionFinder();
        newVersions.addAll(javaVersionFinder.checkDirectory(javaScanner, cfg.getFileManager().getDir("java").toPath(), os));

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
        nanos = System.nanoTime() - nanos;
        log.debug("Java refresh took " + (nanos / 1_000_000.0) + "ms.");
        return newVersions;
    }

    public void refreshHeadlessMcJavaVersions() {
        Set<Java> versions = new HashSet<>(contents);
        boolean addFilePermissions = os.getType() == OS.Type.LINUX || os.getType() == OS.Type.OSX;
        JavaScanner javaScanner = JavaScanner.of(new JavaVersionParser(addFilePermissions));
        JavaVersionFinder javaVersionFinder = new JavaVersionFinder();
        versions.addAll(javaVersionFinder.checkDirectory(javaScanner, cfg.getFileManager().getDir("java").toPath(), os, versions));
        contents = versions;
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
        ensureInitialized();
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

            int hyphen = version.indexOf("-"); // 21-internal
            if (hyphen != -1) {
                version = version.substring(0, hyphen);
            }
        }

        return Integer.parseInt(version);
    }

}
