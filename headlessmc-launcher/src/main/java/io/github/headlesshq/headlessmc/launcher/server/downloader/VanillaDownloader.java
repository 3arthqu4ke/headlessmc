package io.github.headlesshq.headlessmc.launcher.server.downloader;

import lombok.RequiredArgsConstructor;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.server.ServerTypeDownloader;
import io.github.headlesshq.headlessmc.launcher.version.Version;
import io.github.headlesshq.headlessmc.launcher.version.VersionExecutable;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@RequiredArgsConstructor
public class VanillaDownloader implements ServerTypeDownloader {
    @Override
    public DownloadHandler download(Launcher launcher, String version, @Nullable String typeVersion, String... args) throws IOException {
        if (typeVersion != null) {
            throw new IOException("Vanilla downloads do not support type versions like '" + typeVersion + "'");
        }

        Version parsedVersion = VersionUtil.getVersion(launcher, version);
        VersionExecutable serverDownload = parsedVersion.getServerDownload();
        if (serverDownload == null) {
            throw new IOException("Failed to find server download for version '" + version + "'");
        }

        return new UrlJarDownloadHandler(launcher.getDownloadService(), serverDownload.getUrl(), null);
    }

}
