package me.earth.headlessmc.java.download;

import me.earth.headlessmc.api.HeadlessMcApi;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface JavaDownloader {
    default void download(JavaDownloadRequest version) throws IOException {
        download(Paths.get(HeadlessMcApi.NAME).resolve("java"), version);
    }

    void download(Path javaVersionsDir, JavaDownloadRequest request) throws IOException;

}
