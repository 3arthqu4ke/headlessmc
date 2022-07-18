package me.earth.headlessmc.command;

import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.MockedHeadlessMc;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.security.Permission;

import static org.junit.jupiter.api.Assertions.*;

public class QuitCommandTest {
    private final QuitCommand command =
        new QuitCommand(MockedHeadlessMc.INSTANCE);

    @Test
    public void testMatches() {
        assertTrue(command.matches("quit"));
        assertTrue(command.matches("Quit"));
        assertTrue(command.matches("QUIT "));
        assertTrue(command.matches("exit"));
        assertTrue(command.matches("stop"));

        assertFalse(command.matches("not quit"));
        assertFalse(command.matches());
    }

    @Test
    @Disabled("Disabled for now until I can verify it works everywhere")
    public void testExecute() {
        try {
            System.setSecurityManager(new SecurityManager() {
                @Override
                public void checkPermission(Permission perm) {

                }

                @Override
                public void checkExit(int status) {
                    super.checkExit(status);
                    throw new ExitException(status);
                }
            });

            val ex = assertThrows(ExitException.class,
                                  () -> command.execute("quit"));
            assertEquals(0, ex.code);
        } finally {
            System.setSecurityManager(null);
        }
    }

    @RequiredArgsConstructor
    public static final class ExitException extends RuntimeException {
        private final int code;
    }

}
