package me.earth.headlessmc.launcher.command.download;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.modlauncher.Modlauncher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VersionArgumentTest {
    @Test
    public void testVersionArgument() throws CommandException {
        assertThrows(CommandException.class, () -> VersionArgument.parseVersion("test:1.12.2"));
        assertThrows(CommandException.class, () -> VersionArgument.parseVersion("fabric:1.12.2:dummy"));

        VersionArgument versionArgument = VersionArgument.parseVersion("1.12.2");
        assertEquals("1.12.2", versionArgument.getName());
        assertNull(versionArgument.getModlauncher());

        versionArgument = VersionArgument.parseVersion("fabric:1.12.2");
        assertEquals("1.12.2", versionArgument.getName());
        assertEquals(Modlauncher.FABRIC, versionArgument.getModlauncher());

        versionArgument = VersionArgument.parseVersion("forge:1.21.2");
        assertEquals("1.21.2", versionArgument.getName());
        assertEquals(Modlauncher.LEXFORGE, versionArgument.getModlauncher());

        versionArgument = VersionArgument.parseVersion("neoforge:some_snapshot");
        assertEquals("some_snapshot", versionArgument.getName());
        assertEquals(Modlauncher.NEOFORGE, versionArgument.getModlauncher());
    }

}
