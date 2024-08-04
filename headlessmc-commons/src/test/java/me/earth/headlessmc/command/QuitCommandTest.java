package me.earth.headlessmc.command;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.MockedHeadlessMc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @RequiredArgsConstructor
    public static final class ExitException extends RuntimeException {
        private final int code;
    }

}
