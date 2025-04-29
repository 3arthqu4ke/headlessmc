package me.earth.headlessmc.launcher.command.download;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DownloadCommandTest {
    @Test
    public void testDownloadCommand() {
        Launcher launcher = LauncherMock.create();
        DummyVersionInfoCache cache = new DummyVersionInfoCache();
        launcher.setVersionInfoCache(cache);
        DownloadCommand downloadCommand = new DownloadCommand(launcher);
        assertThrows(CommandException.class, () -> downloadCommand.download("0"));
    }

}
