package me.earth.headlessmc.launcher.server.downloader;

import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.download.VersionInfo;
import me.earth.headlessmc.launcher.command.download.VersionInfoUtil;
import me.earth.headlessmc.launcher.version.Version;

import java.io.IOException;

public class VersionUtil {
    public static Version getVersion(Launcher launcher, String version) throws IOException {
        VersionInfo versionInfo = launcher.getVersionInfoCache().getByName(version);
        if (versionInfo == null) {
            throw new IOException("Failed to find version '" + version + "'");
        }

        return VersionInfoUtil.toVersion(
                versionInfo, launcher.getVersionService(), launcher.getDownloadService());
    }

}
