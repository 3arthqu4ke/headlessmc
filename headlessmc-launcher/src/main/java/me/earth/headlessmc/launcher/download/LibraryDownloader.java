package me.earth.headlessmc.launcher.download;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.util.URLs;
import me.earth.headlessmc.launcher.version.Library;
import me.earth.headlessmc.os.OS;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

@CustomLog
@RequiredArgsConstructor
public class LibraryDownloader {
    private static final URL MAVEN_CENTRAL = URLs.url("https://repo1.maven.org/maven2/");

    private final DownloadService downloadService;
    private final HasConfig config;
    private final OS os;

    @Setter
    private boolean shouldLog = true;

    public void download(Library library, Path to) throws IOException {
        String libPath = library.getPath(os);
        if (fixArmLibrary(library, libPath, to)) {
            return;
        }

        String url = library.getUrl(libPath);
        if (shouldLog) {
            log.info(libPath + " is missing, downloading from " + url);
        }

        download(url, to, library.getSha1(), library.getSize());
    }

    private boolean fixArmLibrary(Library library, String libPathIn, Path to) {
        if (os.isArm()
                && os.getType() == OS.Type.LINUX
                && library.isOrContainsNatives(os)
                && config.getConfig().get(LauncherProperties.ARM_FIX_LIBRARIES, true)) {
            String libPath = libPathIn.replace(File.separatorChar, '/');
            if (libPath.contains("com/mojang/jtracy/")) {
                return false; // they do not have other natives
            }

            String fixedLibPath = libPath
                    .replace("x64", "arm64")
                    .replace("x86", "arm32");

            if (fixedLibPath.endsWith("-natives-linux.jar")) {
                fixedLibPath = fixedLibPath
                        .substring(0, fixedLibPath.length() - 4)
                        .concat(os.is64bit() ? "-arm64.jar" : "-arm32.jar");
            }

            if (!fixedLibPath.equals(libPath)) {
                try {
                    log.debug("Fixing library " + libPath + " to " + fixedLibPath);
                    download(MAVEN_CENTRAL + fixedLibPath, to, null, null);
                    return true;
                } catch (IOException e) {
                    log.error("Failed to fix library " + fixedLibPath, e);
                }
            }
        }

        return false;
    }

    public void download(String url, Path to, @Nullable String hash, @Nullable Long size) throws IOException {
        boolean checkHash = config.getConfig().get(LauncherProperties.LIBRARIES_CHECK_HASH, true);
        boolean checkSize = checkHash || config.getConfig().get(LauncherProperties.LIBRARIES_CHECK_SIZE, true);
        Long expectedSize = checkSize ? size : null;
        String expectedHash = checkHash ? hash : null;
        downloadService.download(url, to, expectedSize, expectedHash);
    }

}
