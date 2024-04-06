package me.earth.headlessmc.launcher;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.HeadlessMcImpl;
import me.earth.headlessmc.command.line.CommandLineImpl;
import me.earth.headlessmc.config.HmcProperties;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.auth.AccountStore;
import me.earth.headlessmc.launcher.auth.AccountValidator;
import me.earth.headlessmc.launcher.auth.OfflineChecker;
import me.earth.headlessmc.launcher.command.LaunchContext;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.files.FileUtil;
import me.earth.headlessmc.launcher.files.MinecraftFinder;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.os.OSFactory;
import me.earth.headlessmc.launcher.util.UuidUtil;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.launcher.version.VersionUtil;
import me.earth.headlessmc.logging.LogLevelUtil;
import me.earth.headlessmc.logging.LoggingHandler;
import me.earth.headlessmc.logging.SimpleLog;

import java.io.IOException;

@CustomLog
@UtilityClass
public final class Main {
    public static void main(String[] args) {
        Throwable throwable = null;
        try {
            runHeadlessMc(args);
        } catch (Throwable t) {
            throwable = t;
        } finally {
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
            if (throwable == null) {
                System.exit(0);
            } else {
                throwable.printStackTrace();
                System.exit(-1);
            }
        }
    }

    private void runHeadlessMc(String... args) throws IOException {
        LoggingHandler.apply();
        val files = FileManager.mkdir("HeadlessMC");
        val configs = Service.refresh(new ConfigService(files));
        LogLevelUtil.trySetLevel(
            configs.getConfig().get(HmcProperties.LOGLEVEL, "INFO"));

        val in = new CommandLineImpl();
        val hmc = new HeadlessMcImpl(new SimpleLog(), configs, in);

        val os = OSFactory.detect(configs.getConfig());
        val mcFiles = MinecraftFinder.find(configs.getConfig(), os);
        val versions = Service.refresh(new VersionService(mcFiles));
        val javas = Service.refresh(new JavaService(configs));

        val validator = new AccountValidator();
        val accountStore = new AccountStore(files, configs);
        val accounts = new AccountManager(accountStore, validator, new OfflineChecker(configs));

        val launcher = new Launcher(hmc, versions, mcFiles, files,
                                    new ProcessFactory(mcFiles, configs, os), configs,
                                    javas, accounts, validator);
        LauncherApi.setLauncher(launcher);
        deleteOldFiles(launcher);
        versions.refresh();
        hmc.setCommandContext(new LaunchContext(launcher));

        if (!QuickExitCliHandler.checkQuickExit(launcher, in, args)) {
            log.info(String.format("Detected: %s", os));
            log.info(String.format("Minecraft Dir: %s", mcFiles.getBase()));
            hmc.log(VersionUtil.makeTable(VersionUtil.releases(versions)));
            in.listen(hmc);
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
                    FileUtil.delete(file);
                } catch (IOException ioe) {
                    log.error("Couldn't delete " + file.getName()
                                  + " : " + ioe.getMessage());
                }
            }
        }
    }

}
