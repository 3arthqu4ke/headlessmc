package me.earth.headlessmc.launcher.command.download;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DownloadCommandTest {
    @Test
    public void testDownloadCommand() {
        Launcher launcher = LauncherMock.INSTANCE;
        DummyVersionInfoCache cache = new DummyVersionInfoCache();
        DownloadCommand downloadCommand = new DownloadCommand(launcher, cache);
        assertThrows(CommandException.class, () -> downloadCommand.download("0"));
    }

}
