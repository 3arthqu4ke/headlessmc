package io.github.headlesshq.headlessmc.launcher.command.download;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.launcher.modlauncher.Modlauncher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VersionArgumentTest {
    @Test
    public void testVersionArgument() throws CommandException {
        assertThrows(CommandException.class, () -> VersionArgument.parseVersion("test:1.12.2"));
        assertThrows(CommandException.class, () -> VersionArgument.parseVersion("test:1.12.2:dummy"));
        assertThrows(CommandException.class, () -> VersionArgument.parseVersion("test:1.12.2:dummy:dummy2"));

        VersionArgument versionArgument = VersionArgument.parseVersion("1.12.2");
        assertEquals("1.12.2", versionArgument.getName());
        assertNull(versionArgument.getModlauncher());
        assertNull(versionArgument.getModLauncherVersion());

        versionArgument = VersionArgument.parseVersion("fabric:1.12.2");
        assertEquals("1.12.2", versionArgument.getName());
        assertEquals(Modlauncher.FABRIC, versionArgument.getModlauncher());
        assertNull(versionArgument.getModLauncherVersion());

        versionArgument = VersionArgument.parseVersion("forge:1.21.2");
        assertEquals("1.21.2", versionArgument.getName());
        assertEquals(Modlauncher.LEXFORGE, versionArgument.getModlauncher());
        assertNull(versionArgument.getModLauncherVersion());

        versionArgument = VersionArgument.parseVersion("neoforge:some_snapshot");
        assertEquals("some_snapshot", versionArgument.getName());
        assertEquals(Modlauncher.NEOFORGE, versionArgument.getModlauncher());
        assertNull(versionArgument.getModLauncherVersion());

        versionArgument = VersionArgument.parseVersion("neoforge:some_snapshot:1.0.0");
        assertEquals("some_snapshot", versionArgument.getName());
        assertEquals(Modlauncher.NEOFORGE, versionArgument.getModlauncher());
        assertEquals("1.0.0", versionArgument.getModLauncherVersion());
    }

}
