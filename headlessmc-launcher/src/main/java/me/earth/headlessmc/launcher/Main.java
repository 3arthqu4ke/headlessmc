package me.earth.headlessmc.launcher;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMcImpl;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.auth.AbstractLoginCommand;
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
import me.earth.headlessmc.launcher.os.OSFactory;
import me.earth.headlessmc.launcher.plugin.PluginManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificModManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificMods;
import me.earth.headlessmc.launcher.util.UuidUtil;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.launcher.version.VersionUtil;
import me.earth.headlessmc.logging.LoggingService;
import net.lenni0451.commons.httpclient.constants.Headers;

import java.io.IOException;

@CustomLog
@UtilityClass
public final class Main {
    public static void main(String[] args) {
        ExitManager exitManager = new ExitManager();
        Throwable throwable = null;
        try {
            runHeadlessMc(exitManager, args);
        } catch (Throwable t) {
            throwable = t;
        } finally {
            exitManager.onMainThreadEnd(throwable);
            /*
            These "System.exit()" calls are here because of the LoginCommands
            -webview option. It seems that after closing the JFrame there is
            still either the AWT, Webview or Javafx thread running, keeping the
            program alive. I played around with the code of the OpenAuth lib and
            could not find a good solution. E.g. LoginFrame DISPOSE_ON_CLOSE
            prevents further LoginFrames from getting displayed. The only ok
            option I found was to make the LoginFrame a Singleton and dispose it
            manually at the end but that prevents multiple LoginFrames at the
            same time.
             */
            try {
                if (throwable == null) {
                    exitManager.exit(0);
                } else {
                    log.error(throwable);
                    exitManager.exit(-1);
                }
            } catch (Throwable exitThrowable) {
                // it is possible, if we launch in memory, that forge prevents us from calling System.exit through their SecurityManager
                if (throwable != null && exitThrowable.getClass() == throwable.getClass()) { // we have logged FMLSecurityManager$ExitTrappedException before
                    log.error("Failed to exit!", exitThrowable);
                }

                // TODO: exit gracefully, try to call Forge to exit
            }
        }
    }

    private void runHeadlessMc(ExitManager exitManager, String... args) throws AuthException {
        LoggingService loggingService = new LoggingService();
        loggingService.init();
        AbstractLoginCommand.replaceLogger();

        if (Main.class.getClassLoader() == ClassLoader.getSystemClassLoader()) {
            log.warn("You are not running from headlessmc-launcher-wrapper. Some things will not work properly!");
        }

        val files = FileManager.mkdir("HeadlessMC");

        AutoConfiguration.runAutoConfiguration(files);

        val configs = Service.refresh(new ConfigService(files));
        val hmc = new HeadlessMcImpl(configs, new CommandLine(), exitManager, loggingService);

        val os = OSFactory.detect(configs.getConfig());
        val mcFiles = MinecraftFinder.find(configs.getConfig(), os);
        val gameDir = FileManager.mkdir(configs.getConfig().get(LauncherProperties.GAME_DIR, mcFiles.getPath()));
        val versions = Service.refresh(new VersionService(mcFiles));
        val javas = Service.refresh(new JavaService(configs));

        val accountStore = new AccountStore(files, configs);
        val accounts = new AccountManager(new AccountValidator(), new OfflineChecker(configs), accountStore);
        accounts.load(configs.getConfig());

        DownloadService downloadService = new DownloadService();
        if (configs.getConfig().get(LauncherProperties.HTTP_USER_AGENT_ENABLED, true)) {
            downloadService.setHttpClientFactory(() ->
                downloadService.getDefaultHttpClient()
                    .setHeader(Headers.USER_AGENT, configs.getConfig().get(LauncherProperties.HTTP_USER_AGENT, "Mozilla/5.0")));
        }

        val versionSpecificModManager = new VersionSpecificModManager(downloadService, files.createRelative("specifics"));
        versionSpecificModManager.addRepository(VersionSpecificMods.HMC_SPECIFICS);
        versionSpecificModManager.addRepository(VersionSpecificMods.MC_RUNTIME_TEST);

        val launcher = new Launcher(hmc, versions, mcFiles, gameDir, new ChecksumService(), new DownloadService(), files,
                                    new ProcessFactory(downloadService, mcFiles, configs, os), configs,
                                    javas, accounts, versionSpecificModManager, new PluginManager());
        LauncherApi.setLauncher(launcher);
        deleteOldFiles(launcher);

        LaunchContext launchContext = new LaunchContext(launcher);
        hmc.getCommandLine().setCommandContext(launchContext);
        hmc.getCommandLine().setBaseContext(launchContext);

        if (hmc.getConfig().get(JLineProperties.ENABLED, true)) {
            hmc.getCommandLine().setCommandLineProvider(JLineCommandLineReader::new);
        }

        launcher.getPluginManager().init(launcher);
        if (!QuickExitCliHandler.checkQuickExit(launcher, args)) {
            log.info(String.format("Detected: %s", os));
            log.info(String.format("Minecraft Dir: %s", mcFiles.getBase()));
            hmc.log(VersionUtil.makeTable(VersionUtil.releases(versions)));
            hmc.getCommandLine().read(hmc);
        }
    }

    private void deleteOldFiles(Launcher launcher) {
        if (launcher.getConfig().get(LauncherProperties.KEEP_FILES, false)) {
            return;
        }

        for (val file : launcher.getFileManager().listFiles()) {
            if (file.isDirectory() && UuidUtil.isUuid(file.getName())) {
                try {
                    log.debug("Deleting " + file.getAbsolutePath());
                    launcher.getFileManager().delete(file);
                } catch (IOException ioe) {
                    log.error("Couldn't delete " + file.getName() + " : " + ioe.getMessage());
                }
            }
        }
    }

}
