package me.earth.headlessmc.launcher.download;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.version.Library;
import me.earth.headlessmc.os.OS;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;

@CustomLog
@RequiredArgsConstructor
public class LibraryDownloader {
    private final DownloadService downloadService;
    private final HasConfig config;
    private final OS os;

    @Setter
    private boolean shouldLog = true;

    public void download(Library library, Path to) throws IOException {
        String libPath = library.getPath(os);
        String url = library.getUrl(libPath);
        if (shouldLog) {
            log.info(libPath + " is missing, downloading from " + url);
        }

        download(url, to, library.getSha1(), library.getSize());
    }

    public void download(String url, Path to, @Nullable String hash, @Nullable Long size) throws IOException {
        boolean checkHash = config.getConfig().get(LauncherProperties.LIBRARIES_CHECK_HASH, true);
        boolean checkSize = checkHash || config.getConfig().get(LauncherProperties.LIBRARIES_CHECK_SIZE, true);
        Long expectedSize = checkSize ? size : null;
        String expectedHash = checkHash ? hash : null;
        downloadService.download(url, to, expectedSize, expectedHash);
    }

}
