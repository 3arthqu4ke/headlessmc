package me.earth.headlessmc.launcher;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.HeadlessMcImpl;
import me.earth.headlessmc.command.line.CommandLineImpl;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.command.LaunchContext;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.files.MinecraftFinder;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.os.OSFactory;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.launcher.version.VersionUtil;
import me.earth.headlessmc.logging.LoggingHandler;
import me.earth.headlessmc.logging.SimpleLog;

import java.io.IOException;
import java.util.stream.Collectors;

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
        log.info(String.format("Detected: %s", os));
        val mcFiles = MinecraftFinder.find(configs.getConfig(), os);
        log.info(String.format("Minecraft Directory: %s", mcFiles.getBase()));
        val versions = Service.refresh(new VersionService(mcFiles, configs));
        val javas = Service.refresh(new JavaService(configs));
        val accounts = new AccountManager();

        val launcher = new Launcher(hmc, versions, mcFiles, fileManager,
                                    new ProcessFactory(mcFiles, os), configs,
                                    javas, accounts);
        LauncherApi.setLauncher(launcher);

        versions.refresh();
        hmc.log(VersionUtil.makeTable(VersionUtil.releases(versions)));
        hmc.setCommandContext(new LaunchContext(launcher));
        in.listen(hmc);
    }

}
