package me.earth.headlessmc.launcher.server.downloader;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.command.download.VersionInfo;
import me.earth.headlessmc.launcher.command.download.VersionInfoCache;
import me.earth.headlessmc.launcher.command.download.VersionInfoUtil;
import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.server.ServerTypeDownloader;
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.launcher.version.VersionExecutable;
import me.earth.headlessmc.launcher.version.VersionService;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@RequiredArgsConstructor
public class VanillaDownloader implements ServerTypeDownloader {
    private final DownloadService downloadService;
    private final VersionInfoCache versionInfoCache;
    private final VersionService versionService;

    @Override
    public DownloadHandler download(String version, @Nullable String typeVersion) throws IOException {
        if (typeVersion != null) {
            throw new IOException("Vanilla downloads do not support type versions like '" + typeVersion + "'");
        }

        VersionInfo versionInfo = versionInfoCache.getByName(version);
        if (versionInfo == null) {
            throw new IOException("Failed to find version '" + version + "'");
        }

        Version parsedVersion = VersionInfoUtil.toVersion(versionInfo, versionService, downloadService);
        VersionExecutable serverDownload = parsedVersion.getServerDownload();
        if (serverDownload == null) {
            throw new IOException("Failed to find server download for version '" + version + "'");
        }

        return new UrlJarDownloadHandler(downloadService, serverDownload.getUrl(), null);
    }

}
