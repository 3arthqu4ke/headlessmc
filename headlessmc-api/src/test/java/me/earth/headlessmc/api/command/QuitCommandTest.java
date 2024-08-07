package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.MockedHeadlessMc;
import org.junit.jupiter.api.Test;

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
    public void testExecute() {
        MockedHeadlessMc.INSTANCE.getExitManager().setExitCode(null);
        assertNull(MockedHeadlessMc.INSTANCE.getExitManager().getExitCode());
        command.execute("quit");
        assertEquals(0, MockedHeadlessMc.INSTANCE.getExitManager().getExitCode());
        MockedHeadlessMc.INSTANCE.getExitManager().setExitCode(null);
    }

}
