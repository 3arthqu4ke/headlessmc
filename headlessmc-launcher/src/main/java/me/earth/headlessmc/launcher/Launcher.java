package me.earth.headlessmc.launcher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.launcher.version.VersionService;

/**
 * Represents the HeadlessMc launcher and everything that belongs to it.
 */
@Getter
@Setter
@AllArgsConstructor
public class Launcher implements HeadlessMc {
    /**
     * The current version of HeadlessMc.
     */
    public static final String VERSION = "2.1.0";

    /**
     * A HeadlessMc instance that this Launcher delegates to for CommandLine, ExitManager, etc..
     */
    @Delegate
    private HeadlessMc headlessMc;
    /**
     * Used for finding and managing Minecraft {@link Version}s.
     */
    private VersionService versionService;
    /**
     * The .minecraft directory in which we store assets, libraries and versions.
     */
    private FileManager mcFiles;
    /**
     * The directory in which the game will run.
     */
    private FileManager gameDir;
    /**
     * A service for checking SHA1 hashes.
     */
    private ChecksumService sha1Service;
    /**
     * A service for making GET requests.
     */
    private DownloadService downloadService;
    /**
     * The directory for HeadlessMc files.
     */
    private FileManager fileManager;
    /**
     * Actual Launching happens here.
     */
    private ProcessFactory processFactory;
    /**
     * Manages the HeadlessMc configuration.
     */
    private ConfigService configService;
    /**
     * Manages the Java versions we use for launching.
     */
    private JavaService javaService;
    /**
     * Manages accounts and authentication.
     */
    private AccountManager accountManager;
    /**
     * Manages and downloads version specific mods like the HeadlessMc-Specifics.
     */
    private VersionSpecificModManager versionSpecificModManager;
    /**
     * Manages Plugins for the HeadlessMc launcher.
     */
    private PluginManager pluginManager;

}
