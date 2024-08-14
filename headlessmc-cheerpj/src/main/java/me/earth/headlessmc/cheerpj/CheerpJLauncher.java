package me.earth.headlessmc.cheerpj;

import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.HeadlessMcImpl;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.api.process.InAndOutProvider;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.Service;
import me.earth.headlessmc.launcher.auth.*;
import me.earth.headlessmc.launcher.command.LaunchContext;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.files.FileUtil;
import me.earth.headlessmc.launcher.files.MinecraftFinder;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.os.OSFactory;
import me.earth.headlessmc.launcher.plugin.PluginManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificModManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificMods;
import me.earth.headlessmc.launcher.util.UuidUtil;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.logging.Logger;
import me.earth.headlessmc.logging.LoggerFactory;
import me.earth.headlessmc.logging.LoggingService;
import me.earth.headlessmc.logging.NoThreadFormatter;
import me.earth.headlessmc.runtime.RuntimeProperties;
import me.earth.headlessmc.runtime.commands.RuntimeContext;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

@RequiredArgsConstructor
public class CheerpJLauncher {
    private final InAndOutProvider inAndOutProvider;
    private final CheerpJGUI gui;

    public void launch() {
        LoggingService loggingService = new LoggingService();
        loggingService.setFileHandler(false);
        loggingService.setStreamFactory(() -> inAndOutProvider.getOut().get());
        loggingService.setFormatterFactory(NoThreadFormatter::new);
        loggingService.init();
        loggingService.setLevel(Level.INFO);

        Logger logger = LoggerFactory.getLogger("HeadlessMc");
        logger.info("Initializing HeadlessMc...");

        Properties properties = new Properties();
        initializeProperties(properties);
        Config config = new ConfigImpl(properties, "default", 0);
        HasConfig configs = () -> config;
        CommandLine commandLine = new CommandLine(inAndOutProvider, gui);

        HeadlessMc hmc = new HeadlessMcImpl(configs, commandLine, new ExitManager(), loggingService);
        hmc.getExitManager().setExitManager(i -> logger.info("HeadlessMc exited with code " + i));

        try {
            initialize(hmc, logger);
            logger.info("HeadlessMc initialized.");
        } catch (Throwable t) {
            logger.error("Failed to initialize HeadlessMc", t);
            CommandContext commands = new RuntimeContext(hmc);
            commandLine.setAllContexts(commands);
        }

        gui.getCommandHandler().setValue(commandLine.getCommandConsumer());
    }

    private void initializeProperties(Properties properties) {
        properties.put(RuntimeProperties.DONT_ASK_FOR_QUIT.getName(), "true");
    }

    private void initialize(HeadlessMc hmc, Logger logger) throws AuthException {
        FileManager files = FileManager.mkdir("HeadlessMC");

        val configs = Service.refresh(new ConfigService(files));

        val os = OSFactory.detect(configs.getConfig());
        val mcFiles = MinecraftFinder.find(configs.getConfig(), os);
        val gameDir = FileManager.mkdir(configs.getConfig().get(LauncherProperties.GAME_DIR, mcFiles.getPath()));
        val versions = Service.refresh(new VersionService(mcFiles));
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

        LaunchContext launchContext = new LaunchContext(launcher);
        hmc.getCommandLine().setCommandContext(launchContext);
        hmc.getCommandLine().setBaseContext(launchContext);
    }

    private void deleteOldFiles(Launcher launcher, Logger logger) {
        if (launcher.getConfig().get(LauncherProperties.KEEP_FILES, false)) {
            return;
        }

        for (val file : launcher.getFileManager().listFiles()) {
            if (file.isDirectory() && UuidUtil.isUuid(file.getName())) {
                try {
                    logger.debug("Deleting " + file.getAbsolutePath());
                    FileUtil.delete(file);
                } catch (IOException ioe) {
                    logger.error("Couldn't delete " + file.getName()
                            + " : " + ioe.getMessage());
                }
            }
        }
    }

}
