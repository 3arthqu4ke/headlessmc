package me.earth.headlessmc.launcher.server.downloader;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.server.ServerTypeDownloader;
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.launcher.version.VersionExecutable;
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
