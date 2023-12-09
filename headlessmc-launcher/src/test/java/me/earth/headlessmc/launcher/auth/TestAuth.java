package me.earth.headlessmc.launcher.auth;

import lombok.CustomLog;
import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.TestOfflineChecker;
import me.earth.headlessmc.logging.LogLevelUtil;
import me.earth.headlessmc.logging.LoggingHandler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CustomLog
@Disabled("Authentification tests are only meant to be tested manually.")
public class TestAuth {
    private static final Account DUMMY = new Account("d", "d", "d", "d", "d", "d");
    private static final AccountValidator VALIDATOR = new AccountValidator();

    @Test
    @SneakyThrows
    @Disabled("Authentification tests are only meant to be tested manually.")
    public void testAccountFromConfig() {
        LoggingHandler.apply();
        LogLevelUtil.setLevel(Level.ALL);
        LauncherMock.INSTANCE.getConfigService().refresh();
        val config = LauncherMock.INSTANCE.getConfigService().getConfig();
        val store = new AccountStore(LauncherMock.INSTANCE.getFileManager(),
                                     LauncherMock.INSTANCE);
        val checker = new TestOfflineChecker();
        val manager = new AccountManager(store, VALIDATOR, checker);
        val account = manager.login(config);
        log.info(account.toString());
        assertDoesNotThrow(() -> VALIDATOR.validate(account));
    }

    @Test
    @Disabled("Authentification tests are only meant to be tested manually.")
    public void testFailedValidation() {
        assertThrows(AuthException.class, () -> VALIDATOR.validate(DUMMY));
    }

}
