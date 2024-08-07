package me.earth.headlessmc.launcher;

import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMcImpl;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.api.process.InAndOutProvider;
import me.earth.headlessmc.api.command.line.CommandLineImpl;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.launcher.auth.AccountManager;
import me.earth.headlessmc.launcher.auth.AccountStore;
import me.earth.headlessmc.launcher.auth.AccountValidator;
import me.earth.headlessmc.launcher.auth.ValidatedAccount;
import me.earth.headlessmc.launcher.files.ConfigService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.launch.MockProcessFactory;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.plugin.PluginManager;
import me.earth.headlessmc.launcher.specifics.VersionSpecificModManager;
import me.earth.headlessmc.launcher.version.VersionService;
import me.earth.headlessmc.logging.LoggingService;
import net.raphimc.minecraftauth.step.java.session.StepFullJavaSession;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class LauncherMock {
    public static final Launcher INSTANCE;

    static {
        val base = new FileManager("build");
        val fileManager = base.createRelative("fileManager");
        val configs = new ConfigService(fileManager);
        val in = new CommandLineImpl();
        LoggingService loggingService = new LoggingService();
        val hmc = new HeadlessMcImpl(configs, in, new ExitManager(), loggingService, new InAndOutProvider());

        val os = new OS("windows", OS.Type.WINDOWS, "11", true);
        val mcFiles = base.createRelative("mcFiles");
        val versions = new VersionService(mcFiles);
        val javas = new JavaService(configs);

        val store = new DummyAccountStore(fileManager, configs);
        val accounts = new DummyAccountManager(store, new DummyAccountValidator());

        val versionSpecificModManager = new VersionSpecificModManager(fileManager.createRelative("specifics"));

        INSTANCE = new Launcher(hmc, versions, mcFiles, mcFiles, fileManager,
                                new MockProcessFactory(mcFiles, configs, os), configs,
                                javas, accounts, versionSpecificModManager, new PluginManager());

        INSTANCE.getConfigService().setConfig(ConfigImpl.empty());
    }

    private static final class DummyAccountManager extends AccountManager {
        public DummyAccountManager(AccountStore accountStore, AccountValidator validator) {
            super(validator, new TestOfflineChecker(), accountStore);
        }
    }

    public static final class DummyAccountValidator extends AccountValidator {
        public static final String DUMMY_XUID = "dummy-xuid";

        @Override
        public ValidatedAccount validate(StepFullJavaSession.FullJavaSession session) {
            return new ValidatedAccount(session, DUMMY_XUID);
        }
    }

    public static final class DummyAccountStore extends AccountStore {
        public DummyAccountStore(FileManager fileManager, HasConfig cfg) {
            super(fileManager, cfg);
        }

        @Override
        public List<ValidatedAccount> load() {
            return new ArrayList<>();
        }

        @Override
        public void save(List<ValidatedAccount> accounts) {
            // NOP
        }
    }

}
