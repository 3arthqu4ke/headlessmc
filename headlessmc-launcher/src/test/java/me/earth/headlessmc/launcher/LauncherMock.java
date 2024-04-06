package me.earth.headlessmc.launcher;

import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.HeadlessMcImpl;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.command.line.CommandLineImpl;
import me.earth.headlessmc.config.ConfigImpl;
import me.earth.headlessmc.launcher.auth.Account;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.auth.AccountStore;
import me.earth.headlessmc.launcher.auth.AccountValidator;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.MockProcessFactory;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.logging.SimpleLog;

@UtilityClass
public class LauncherMock {
    public static final Launcher INSTANCE;

    static {
        val base = new FileManager("build");
        val fileManager = base.createRelative("fileManager");
        val configs = new ConfigService(fileManager);
        val in = new CommandLineImpl();
        val hmc = new HeadlessMcImpl(new SimpleLog(), configs, in);

        val os = new OS("windows", OS.Type.WINDOWS, "11", true);
        val mcFiles = base.createRelative("mcFiles");
        val versions = new VersionService(mcFiles);
        val javas = new JavaService(configs);

        val store = new AccountStore(fileManager, configs);
        val validator = new AccountValidator();
        val accounts = new DummyAccountManager(store, validator);

        INSTANCE = new Launcher(hmc, versions, mcFiles, fileManager,
                                new MockProcessFactory(mcFiles, configs, os), configs,
                                javas, accounts, validator);

        INSTANCE.getConfigService().setConfig(ConfigImpl.empty());
    }

    private static final class DummyAccountManager extends AccountManager {
        public DummyAccountManager(AccountStore accountStore,
                                   AccountValidator validator) {
            super(accountStore, validator, new TestOfflineChecker());
        }

        @Override
        public Account login(Config config) {
            return new Account("d", "d", "d", "d", "d", "d");
        }
    }

}
