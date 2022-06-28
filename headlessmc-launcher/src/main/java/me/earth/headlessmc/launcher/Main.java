package me.earth.headlessmc.launcher;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.HeadlessMcImpl;
import me.earth.headlessmc.command.line.CommandLineImpl;
import me.earth.headlessmc.command.line.Listener;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.command.LaunchContext;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.files.MinecraftFinder;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.os.OSFactory;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.launcher.version.VersionUtil;
import me.earth.headlessmc.logging.LoggingHandler;
import me.earth.headlessmc.logging.SimpleLog;

import java.io.IOException;

@CustomLog
@UtilityClass
public final class Main {
    public static void main(String[] args) throws IOException {
        LoggingHandler.apply();
        val fileManager = FileManager.mkdir("HeadlessMC");
        val configs = Service.refresh(new ConfigService(fileManager));
        val in = new CommandLineImpl();
        val hmc = new HeadlessMcImpl(new SimpleLog(), configs, in);

        val os = OSFactory.detect(configs.getConfig());
        val mcFiles = MinecraftFinder.find(configs.getConfig(), os);
        val versions = Service.refresh(new VersionService(mcFiles));
        val javas = Service.refresh(new JavaService(configs));
        val accounts = new AccountManager();

        val launcher = new Launcher(hmc, versions, mcFiles, fileManager,
                                    new ProcessFactory(mcFiles, os), configs,
                                    javas, accounts);
        LauncherApi.setLauncher(launcher);
        versions.refresh();
        hmc.setCommandContext(new LaunchContext(launcher));

        if (isQuickExitCli(args, launcher, in)) {
            return;
        }

        log.info(String.format("Detected: %s", os));
        log.info(String.format("Minecraft Directory: %s", mcFiles.getBase()));
        hmc.log(VersionUtil.makeTable(VersionUtil.releases(versions)));
        in.listen(hmc);
    }

    private boolean isQuickExitCli(String[] args, Launcher launcher,
                                   Listener in) {
        var quickExitCli = false;
        val cmd = new StringBuilder();
        for (val arg : args) {
            if (arg == null) {
                continue;
            }

            if (arg.equalsIgnoreCase("--version")) {
                launcher.log("HeadlessMc - " + Launcher.VERSION);
                return true;
            }

            if (quickExitCli) {
                cmd.append(arg).append(" ");
            }

            if (arg.equalsIgnoreCase("--command")) {
                quickExitCli = true;
            }
        }

        if (quickExitCli) {
            launcher.setQuickExitCli(true);
            launcher.getCommandContext().execute(cmd.toString());
            if (launcher.isWaitingForInput()) {
                in.listen(launcher);
            }
        }

        return quickExitCli;
    }

}
