package me.earth.headlessmc.web.cheerpj.plugin;

import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.HeadlessMcImpl;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CopyContext;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.command.line.CommandLineReader;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.api.process.InAndOutProvider;
import me.earth.headlessmc.auth.AbstractLoginCommand;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.Service;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.auth.AccountStore;
import me.earth.headlessmc.launcher.auth.AccountValidator;
import me.earth.headlessmc.launcher.auth.OfflineChecker;
import me.earth.headlessmc.launcher.command.LaunchContext;
import me.earth.headlessmc.launcher.download.ChecksumService;
import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.files.MinecraftFinder;
import me.earth.headlessmc.launcher.java.JavaService;
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
import net.lenni0451.commons.httpclient.constants.ContentTypes;
import net.lenni0451.commons.httpclient.constants.Headers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

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
        loggingService.setLevel(Level.INFO);

        Logger logger = LoggerFactory.getLogger("HeadlessMc");
        logger.info("Initializing HeadlessMc...");
        logger.info("This is just a demo!");
        logger.info("You can find the javadocs at https://3arthqu4ke.github.io/headlessmc/javadoc");
        logger.info("Powered by CheerpJ!");
        logger.info("https://cheerpj.com/");
        logger.info("CheerpJ currently does not support some features we need, so you will probably not be able to launch the game yet!");
        logger.info("Display Size: " + gui.getFrame().getWidth() + "x" + gui.getFrame().getHeight());
        logger.warn("HeadlessMc is running in a browser and will not be able to make CORS requests.");
        logger.warn("You will need a CORS proxy or plugin.");

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

        @SuppressWarnings("resource")
        ExecutorService service = Executors.newSingleThreadExecutor(CommandLineReader.DEFAULT_THREAD_FACTORY);
        gui.getCommandHandler().set(str -> service.submit(() -> {
            try {
                commandLine.getCommandConsumer().accept(str);
            } catch (Throwable t) {
                logger.error("Failed to execute command " + str, t);
            }
        }));
    }

    private void initializeProperties(Path root) {
        System.setProperty(RuntimeProperties.DONT_ASK_FOR_QUIT.getName(), "true");
        System.setProperty(LauncherProperties.MC_DIR.getName(), root.resolve("mc").toString());
        System.setProperty(LauncherProperties.GAME_DIR.getName(), root.resolve("mc").toString());
        System.setProperty(LauncherProperties.DUMMY_ASSETS.getName(), "true");
        System.setProperty(LauncherProperties.ASSETS_PARALLEL.getName(), "true");
        System.setProperty(LauncherProperties.EXTRACTED_FILE_CACHE_UUID.getName(), CACHE_UUID.toString());
        System.setProperty(LauncherProperties.ALWAYS_IN_MEMORY.getName(), "true");
        System.setProperty(LauncherProperties.IN_MEMORY_REQUIRE_CORRECT_JAVA.getName(), "false");
        // TODO: it the assets index file always corrupts on CheerpJ for some reason
        System.setProperty(LauncherProperties.ALWAYS_DOWNLOAD_ASSETS_INDEX.getName(), "true");
        System.setProperty(LauncherProperties.HTTP_USER_AGENT_ENABLED.getName(), "false");
    }

    private void initialize(HeadlessMc hmc, Logger logger, Path headlessMcRoot) {
        Security.addProvider(new BouncyCastleProvider());
        FileManager.setFactory(CheerpJFileManager::new);
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
        // accounts.load(configs.getConfig()); // CheerpJ doesnt support logging in right now
        accounts.getOfflineChecker().setOffline(true);

        DownloadService downloadService = new DownloadService();
        val versionSpecificModManager = new VersionSpecificModManager(downloadService, files.createRelative("specifics"));
        versionSpecificModManager.addRepository(VersionSpecificMods.HMC_SPECIFICS);
        versionSpecificModManager.addRepository(VersionSpecificMods.MC_RUNTIME_TEST);

        val launcher = new Launcher(hmc, versions, mcFiles, gameDir,
                new ChecksumService(), downloadService,
                files, new CheerpJProcessFactory(downloadService, mcFiles, configs, os), configs,
                javas, accounts, versionSpecificModManager, new PluginManager());

        deleteOldFiles(launcher, logger);
        System.setProperty(LauncherProperties.KEEP_FILES.getName(), "true");

        LaunchContext launchContext = new LaunchContext(launcher);
        hmc.getCommandLine().setAllContexts(launchContext);
        CopyContext copyContext = new CopyContext(hmc, true);
        copyContext.add(new FilesCommand(launcher));
        copyContext.add(new ResizeCommand(hmc, gui));
        copyContext.add(new ClearCommand(hmc, gui));
        hmc.getCommandLine().setAllContexts(copyContext);
        for (Command command : copyContext) {
            if (command instanceof AbstractLoginCommand) {
                // The default HttpClient sets a user agent which causes the browser to make a CORS preflight check
                ((AbstractLoginCommand) command).setHttpClientFactory(() ->
                    downloadService.getDefaultHttpClient()
                        .setFollowRedirects(false)
                        .setHeader(Headers.ACCEPT, ContentTypes.APPLICATION_JSON.toString())
                        .setHeader(Headers.ACCEPT_LANGUAGE, "en-US,en"));
            }
        }

        // launcher.getPluginManager().init(launcher);
        launcher.getPluginManager().getPlugins().add(new CheerpJPlugin());
        hmc.log(VersionUtil.makeTable(VersionUtil.releases(versions)));
    }

    private void deleteOldFiles(Launcher launcher, Logger logger) {
        if (launcher.getConfig().get(LauncherProperties.KEEP_FILES, true)) {
            return;
        }

        for (val file : launcher.getFileManager().listFiles()) {
            if (file.isDirectory() && !CACHE_UUID.toString().equals(file.getName()) && UuidUtil.isUuid(file.getName())) {
                try {
                    logger.debug("Deleting " + file.getAbsolutePath());
                    launcher.getFileManager().delete(file);
                } catch (IOException ioe) {
                    logger.error("Couldn't delete " + file.getName(), ioe);
                }
            }
        }
    }

}
