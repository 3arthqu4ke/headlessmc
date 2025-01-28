package me.earth.headlessmc.java.download;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JavaDownloaderManager implements JavaDownloader {
    public static final String DEFAULT_DISTRIBUTION = "temurin";

    private final Map<String, JavaDownloader> distributions = new HashMap<>();

    public void register(String distribution, @Nullable JavaDownloader downloader) {
        if (downloader == this) {
            throw new IllegalArgumentException("Cannot register JavaDownloaderManager on itself!");
        }

        if (downloader == null) {
            distributions.remove(distribution.toLowerCase(Locale.ENGLISH));
        } else {
            distributions.put(distribution.toLowerCase(Locale.ENGLISH), downloader);
        }
    }

    public void download(Path javaVersionsDir, JavaDownloadRequest request) throws IOException {
        if (Files.exists(javaVersionsDir) && !Files.isDirectory(javaVersionsDir)) {
            throw new IllegalArgumentException(javaVersionsDir.toAbsolutePath() + " is not a directory");
        }

        if (!Files.exists(javaVersionsDir)) {
            Files.createDirectories(javaVersionsDir);
        }

        JavaDownloader downloader = distributions.get(request.getDistribution() == null
                ? DEFAULT_DISTRIBUTION
                : request.getDistribution().toLowerCase(Locale.ENGLISH));

        if (downloader == null) {
            throw new IOException("Failed to find downloader for distribution " + request.getDistribution() + ", available: " + distributions.keySet());
        }

        downloader.download(javaVersionsDir, request);
    }

    public static JavaDownloaderManager getDefault() {
        JavaDownloaderManager manager = new JavaDownloaderManager();
        manager.register(DEFAULT_DISTRIBUTION, new TemurinDownloader());
        return manager;
    }

}
