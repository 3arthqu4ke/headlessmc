package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.MockedHeadlessMc;
import me.earth.headlessmc.api.command.impl.QuitCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuitCommandTest {
    private final QuitCommand command =
        new QuitCommand(MockedHeadlessMc.INSTANCE);

    @Test
    public void testMatches() {
        assertTrue(command.matches("quit", "quit"));
        assertTrue(command.matches("Quit", "Quit"));
        assertTrue(command.matches("QUIT ", "QUIT "));
        assertTrue(command.matches("exit", "exit"));
        assertTrue(command.matches("stop", "stop"));

        assertFalse(command.matches("not quit", "not quit"));
        assertFalse(command.matches(""));
    }

    @Test
    public void testExecute() {
        MockedHeadlessMc.INSTANCE.getExitManager().setExitCode(null);
        assertNull(MockedHeadlessMc.INSTANCE.getExitManager().getExitCode());
        command.execute("quit", "quit");
        assertEquals(0, MockedHeadlessMc.INSTANCE.getExitManager().getExitCode());
        MockedHeadlessMc.INSTANCE.getExitManager().setExitCode(null);
    }

}
