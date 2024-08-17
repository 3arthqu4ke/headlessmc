package me.earth.headlessmc.launcher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.download.ChecksumService;
import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.plugin.PluginManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificModManager;
import me.earth.headlessmc.launcher.version.VersionService;

@Getter
@RequiredArgsConstructor
public class Launcher implements HeadlessMc {
    public static final String VERSION = "2.1.0-SNAPSHOT";

    @Delegate
    private final HeadlessMc headlessMc;
    private final VersionService versionService;
    private final FileManager mcFiles;
    private final FileManager gameDir;
    private final ChecksumService sha1Service;
    private final DownloadService downloadService;
    private final FileManager fileManager;
    private final ProcessFactory processFactory;
    private final ConfigService configService;
    private final JavaService javaService;
    private final AccountManager accountManager;
    private final VersionSpecificModManager versionSpecificModManager;
    private final PluginManager pluginManager;

}
