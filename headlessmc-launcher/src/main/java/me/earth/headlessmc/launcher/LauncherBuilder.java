package me.earth.headlessmc.launcher;

import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.HeadlessMcImpl;
import me.earth.headlessmc.api.classloading.Deencapsulator;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.jline.JLineCommandLineReader;
import me.earth.headlessmc.jline.JLineProperties;
import me.earth.headlessmc.launcher.auth.*;
import me.earth.headlessmc.launcher.command.LaunchContext;
import me.earth.headlessmc.launcher.download.ChecksumService;
import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.files.AutoConfiguration;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.files.MinecraftFinder;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.os.OSFactory;
import me.earth.headlessmc.launcher.plugin.PluginManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificModManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificMods;
import me.earth.headlessmc.launcher.util.UuidUtil;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.logging.LoggingService;
import net.lenni0451.commons.httpclient.constants.Headers;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Helps with building and initializing {@link Launcher} instances.
 */
@Getter
@Setter
@CustomLog
@Accessors(fluent = true, chain = true)
public class LauncherBuilder {
    private DownloadService downloadService = new DownloadService();
    private Deencapsulator deencapsulator = new Deencapsulator();
    private LoggingService loggingService = new LoggingService();
    private ChecksumService sha1Service = new ChecksumService();
    private ExitManager exitManager = new ExitManager();
    private CommandLine commandLine = new CommandLine();
    private PluginManager pluginManager = new PluginManager();

    private FileManager fileManager;
    private FileManager mcFiles;
    private FileManager gameDir;

    private HeadlessMc headlessMc;
    private VersionService versionService;
    private ProcessFactory processFactory;
    private ConfigService configService;
    private JavaService javaService;
    private AccountManager accountManager;
    private VersionSpecificModManager versionSpecificModManager;

    private OS os;

    public LauncherBuilder initLogging() {
        if (!loggingService.isInitialized()) {
            loggingService.init();
        }

        return this;
    }

    public LauncherBuilder initFileManager() {
        return ifNull(LauncherBuilder::fileManager, LauncherBuilder::fileManager, () -> FileManager.mkdir("HeadlessMC"));
    }

    public LauncherBuilder runAutoConfiguration() {
        AutoConfiguration.runAutoConfiguration(requireNonNull(this.fileManager, "FileManager not initialized"));
        return this;
    }

    public LauncherBuilder initConfigService() {
        return ifNull(LauncherBuilder::configService, LauncherBuilder::configService,
                () ->  Service.refresh(new ConfigService(requireNonNull(this.fileManager, "FileManager not initialized"))));
    }

    public LauncherBuilder initHmcInstance() {
        return ifNull(LauncherBuilder::headlessMc, LauncherBuilder::headlessMc,
                () -> new HeadlessMcImpl(requireNonNull(configService, "ConfigHolder was null!"), commandLine, exitManager, loggingService));
    }

    public LauncherBuilder configureDownloadService() {
        if (requireNonNull(configService, "ConfigHolder was null!").getConfig().get(LauncherProperties.HTTP_USER_AGENT_ENABLED, true)) {
            downloadService.setHttpClientFactory(() ->
                    downloadService.getDefaultHttpClient()
                            .setHeader(Headers.USER_AGENT, configService.getConfig().get(LauncherProperties.HTTP_USER_AGENT, "Mozilla/5.0")));
        }

        return this;
    }

    public LauncherBuilder initDefaultServices() {
        return ifNull(LauncherBuilder::os, LauncherBuilder::os, () -> OSFactory.detect(requireNonNull(configService, "ConfigHolder was null!").getConfig()))
            .ifNull(LauncherBuilder::mcFiles, LauncherBuilder::mcFiles, () -> MinecraftFinder.find(configService.getConfig(), os))
            .ifNull(LauncherBuilder::gameDir, LauncherBuilder::gameDir, () -> FileManager.mkdir(configService.getConfig().get(LauncherProperties.GAME_DIR, mcFiles.getPath())))
            .ifNull(LauncherBuilder::versionService, LauncherBuilder::versionService, () -> new VersionService(mcFiles))
            .ifNull(LauncherBuilder::javaService, LauncherBuilder::javaService, () -> new JavaService(configService));
    }

    public LauncherBuilder initAccountManager() throws AuthException {
        if (this.accountManager == null) {
            FileManager fileManager = requireNonNull(this.fileManager, "FileManager was null!");
            ConfigService configService = requireNonNull(this.configService, "ConfigHolder was null!");
            AccountStore accountStore = new AccountStore(fileManager, configService);
            this.accountManager = new AccountManager(new AccountValidator(), new OfflineChecker(configService), accountStore);
            accountManager.load(configService.getConfig());
        }

        return this;
    }

    public LauncherBuilder configureVersionSpecificModManager() {
        if (this.versionSpecificModManager == null) {
            FileManager fileManager = requireNonNull(this.fileManager, "FileManager was null!");
            this.versionSpecificModManager = new VersionSpecificModManager(downloadService, fileManager.createRelative("specifics"));
            versionSpecificModManager.addRepository(VersionSpecificMods.HMC_SPECIFICS);
            versionSpecificModManager.addRepository(VersionSpecificMods.MC_RUNTIME_TEST);
        }

        return this;
    }

    public LauncherBuilder configureCommandLineProvider() {
        if (requireNonNull(configService, "ConfigHolder was null!").getConfig().get(JLineProperties.ENABLED, true)) {
            commandLine.setCommandLineProvider(JLineCommandLineReader::new);
        }

        return this;
    }

    public LauncherBuilder configureProcessFactory() {
        if (this.processFactory == null) {
            this.processFactory = new ProcessFactory(
                    requireNonNull(downloadService, "Download Service was null!"),
                    requireNonNull(mcFiles, "McFiles were null!"),
                    requireNonNull(configService, "ConfigHolder was null!"),
                    requireNonNull(os, "OS was null!")
            );
        }

        return this;
    }

    public LauncherBuilder deleteOldFiles() {
        if (requireNonNull(configService, "ConfigHolder was null!").getConfig().get(LauncherProperties.KEEP_FILES, false)) {
            return this;
        }

        for (val file : requireNonNull(fileManager, "FileManager was null!").listFiles()) {
            if (file.isDirectory() && UuidUtil.isUuid(file.getName())) {
                try {
                    log.debug("Deleting " + file.getAbsolutePath());
                    fileManager.delete(file);
                } catch (IOException ioe) {
                    log.error("Couldn't delete " + file.getName() + " : " + ioe.getMessage());
                }
            }
        }

        return this;
    }

    public <T> LauncherBuilder ifNull(Function<LauncherBuilder, T> getter, BiConsumer<LauncherBuilder, T> setter, Supplier<T> def) {
        if (getter.apply(this) == null) {
            setter.accept(this, def.get());
        }

        return this;
    }

    public Launcher buildDefault() throws AuthException {
        return initLogging()
                .initFileManager()
                .runAutoConfiguration()
                .initConfigService()
                .initHmcInstance()
                .initDefaultServices()
                .initAccountManager()
                .configureDownloadService()
                .configureVersionSpecificModManager()
                .configureCommandLineProvider()
                .configureProcessFactory()
                .deleteOldFiles()
                .buildAndConfigureCommandsAndPlugins();
    }

    public Launcher buildAndConfigureCommandsAndPlugins() {
        Launcher launcher = build();
        LauncherApi.setLauncher(launcher);
        LaunchContext launchContext = new LaunchContext(launcher);
        launcher.getCommandLine().setAllContexts(launchContext);
        launcher.getPluginManager().init(launcher);
        return launcher;
    }

    public Launcher build() {
        return new Launcher(
                requireNonNull(headlessMc, "HeadlessMc was null!"),
                requireNonNull(versionService, "VersionService was null!"),
                requireNonNull(mcFiles, "McFiles were null!"),
                requireNonNull(gameDir, "GameDir was null!"),
                requireNonNull(sha1Service, "Sha1Service was null!"),
                requireNonNull(downloadService, "Download Service was null"),
                requireNonNull(fileManager, "FileManager was null!"),
                requireNonNull(processFactory, "ProcessFactory was null!"),
                requireNonNull(configService, "ConfigService was null!"),
                requireNonNull(javaService, "JavaService was null!"),
                requireNonNull(accountManager, "AccountManager was null!"),
                requireNonNull(versionSpecificModManager, "VersionSpecificModManager was null!"),
                requireNonNull(pluginManager, "PluginManager was null!")
        );
    }

}
