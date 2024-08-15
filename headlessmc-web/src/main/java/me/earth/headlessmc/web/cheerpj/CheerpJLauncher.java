package me.earth.headlessmc.web.cheerpj;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.HeadlessMcImpl;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CopyContext;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.api.process.InAndOutProvider;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.Service;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.auth.AccountStore;
import me.earth.headlessmc.launcher.auth.AccountValidator;
import me.earth.headlessmc.launcher.auth.OfflineChecker;
import me.earth.headlessmc.launcher.command.LaunchContext;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.files.MinecraftFinder;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.os.OSFactory;
import me.earth.headlessmc.launcher.plugin.PluginManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificModManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificMods;
import me.earth.headlessmc.launcher.util.UuidUtil;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.launcher.version.VersionUtil;
import me.earth.headlessmc.logging.Logger;
import me.earth.headlessmc.logging.LoggerFactory;
import me.earth.headlessmc.logging.LoggingService;
import me.earth.headlessmc.logging.NoThreadFormatter;
import me.earth.headlessmc.runtime.RuntimeProperties;
import me.earth.headlessmc.runtime.commands.RuntimeContext;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

@Slf4j
@RequiredArgsConstructor
public class CheerpJLauncher {
    private static final UUID CACHE_UUID = UUID.fromString("e75fc20d-e629-4bf9-a236-c7acb4e9e0af");
    private final InAndOutProvider inAndOutProvider;
    private final CheerpJGUI gui;

    public void launch() {
        // https://cheerpj.com/docs/guides/File-System-support.html#files-mount-point
        Path root = Paths.get("").toAbsolutePath();
        Path headlessMcRoot = root.resolve("HeadlessMC");

        LoggingService loggingService = new LoggingService();
        loggingService.setPathFactory(() -> headlessMcRoot.resolve("headlessmc.log"));
        loggingService.setStreamFactory(() -> inAndOutProvider.getOut().get());
        loggingService.setFormatterFactory(NoThreadFormatter::new);
        loggingService.init();
        loggingService.setLevel(Level.FINE);

        Logger logger = LoggerFactory.getLogger("HeadlessMc");
        logger.info("Initializing HeadlessMc...");
        logger.info("Display Size: " + gui.getFrame().getWidth() + "x" + gui.getFrame().getHeight());
        logger.warn("HeadlessMc is running in a browser and will not be able to make CORS requests.");

        initializeProperties(root);
        Config config = ConfigImpl.empty();
        HasConfig configs = () -> config;
        CommandLine commandLine = new CommandLine(inAndOutProvider, gui);
        HeadlessMc hmc = new HeadlessMcImpl(configs, commandLine, new ExitManager(), loggingService);
        hmc.getExitManager().setExitManager(i -> {
            if (i != 0) {
                logger.info("Error code " + i);
            }
        });

        try {
            initialize(hmc, logger, headlessMcRoot);
            logger.info("HeadlessMc initialized.");
        } catch (Throwable t) {
            logger.error("Failed to initialize HeadlessMc", t);
            CommandContext commands = new RuntimeContext(hmc);
            commandLine.setAllContexts(commands);
        }

        @SuppressWarnings("resource") ExecutorService service = Executors.newSingleThreadExecutor();
        gui.getCommandHandler().setValue(str -> service.submit(() -> commandLine.getCommandConsumer().accept(str)));
    }

    private void initializeProperties(Path root) {
        System.setProperty(RuntimeProperties.DONT_ASK_FOR_QUIT.getName(), "true");
        System.setProperty(LauncherProperties.MC_DIR.getName(), root.resolve("mc").toString());
        System.setProperty(LauncherProperties.GAME_DIR.getName(), root.resolve("mc").toString());
        System.setProperty(LauncherProperties.DUMMY_ASSETS.getName(), "true");
        System.setProperty(LauncherProperties.ASSETS_PARALLEL.getName(), "false");
    }

    private void initialize(HeadlessMc hmc, Logger logger, Path headlessMcRoot) {
        FileManager files = FileManager.mkdir(headlessMcRoot.toString());

        val configs = Service.refresh(new ConfigService(files));

        val os = OSFactory.detect(configs.getConfig());
        val mcFiles = MinecraftFinder.find(configs.getConfig(), os);
        val gameDir = FileManager.mkdir(configs.getConfig().get(LauncherProperties.GAME_DIR, mcFiles.getPath()));
        val versions = new VersionService(mcFiles);
        versions.setRetries(10);
        versions.refresh();

        val javas = Service.refresh(new JavaService(configs));

        val accountStore = new AccountStore(files, configs);
        val accounts = new AccountManager(new AccountValidator(), new OfflineChecker(configs), accountStore);
        accounts.getOfflineChecker().setOffline(true);

        val versionSpecificModManager = new VersionSpecificModManager(files.createRelative("specifics"));
        versionSpecificModManager.addRepository(VersionSpecificMods.HMC_SPECIFICS);
        versionSpecificModManager.addRepository(VersionSpecificMods.MC_RUNTIME_TEST);

        val launcher = new Launcher(hmc, versions, mcFiles, gameDir, files,
                new ProcessFactory(mcFiles, configs, os), configs,
                javas, accounts, versionSpecificModManager, new PluginManager());

        deleteOldFiles(launcher, logger);

        LaunchContext launchContext = new LaunchContext(launcher, false);
        hmc.getCommandLine().setAllContexts(launchContext);
        CopyContext copyContext = new CopyContext(hmc, true);
        copyContext.add(new FilesCommand(launcher));
        copyContext.add(new ResizeCommand(hmc, gui));
        hmc.getCommandLine().setAllContexts(copyContext);

        hmc.log(VersionUtil.makeTable(VersionUtil.releases(versions)));
    }

    private void deleteOldFiles(Launcher launcher, Logger logger) {
        if (launcher.getConfig().get(LauncherProperties.KEEP_FILES, false)) {
            return;
        }

        for (val file : launcher.getFileManager().listFiles()) {
            if (file.isDirectory() && UuidUtil.isUuid(file.getName())) {
                try {
                    logger.debug("Deleting " + file.getAbsolutePath());
                    launcher.getFileManager().delete(file);
                } catch (IOException ioe) {
                    // TODO: CheerpJ cannot delete directories
                    // logger.error("Couldn't delete " + file.getName(), ioe);
                }
            }
        }
    }

}
