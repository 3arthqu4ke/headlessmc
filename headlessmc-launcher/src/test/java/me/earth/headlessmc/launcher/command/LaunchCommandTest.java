package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.LauncherMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class LaunchCommandTest {
    @Test
    public void testLaunchCommand() {
        LaunchCommand command = new LaunchCommand(LauncherMock.INSTANCE);
        assertThrows(CommandException.class, () -> command.execute("launch"));
        // More extensive testing is done in the launch.LaunchTest
    }

}
