package me.earth.headlessmc.launcher.server;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.download.DownloadService;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface ServerTypeDownloader {
    DownloadHandler download(String version, @Nullable String typeVersion) throws IOException;

    interface DownloadHandler {
        Path download(TypeVersionToDownloadPathResolve typeVersionToDownloadPathResolve) throws IOException;
    }

    @FunctionalInterface
    interface TypeVersionToDownloadPathResolve {
        Path resolve(@Nullable String typeVersion) throws IOException;
    }

    @RequiredArgsConstructor
    class UrlJarDownloadHandler implements DownloadHandler {
        private final DownloadService downloadService;
        private final String url;
        private final @Nullable String typeVersion;

        @Override
        public Path download(TypeVersionToDownloadPathResolve typeVersionToDownloadPathResolve) throws IOException {
            Path path = typeVersionToDownloadPathResolve.resolve(typeVersion);
            Files.createDirectories(path);
            downloadService.download(url, path.resolve("server.jar"));
            return path;
        }
    }

}
