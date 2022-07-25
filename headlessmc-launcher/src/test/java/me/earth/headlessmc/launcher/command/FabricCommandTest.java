package me.earth.headlessmc.launcher.command;

import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.UsesResources;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.version.ParsesVersions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FabricCommandTest implements UsesResources, ParsesVersions {
    @Test
    public void testDownloadFailure() {
        val command = new FabricCommand(LauncherMock.INSTANCE);
        System.setProperty(LauncherProperties.FABRIC_URL.getName(), "invalid");
        assertEquals("invalid", LauncherMock.INSTANCE
            .getConfig().get(LauncherProperties.FABRIC_URL));

        val version = parseVersion(getJsonObject("version_parent.json"));
        assertThrows(CommandException.class, () -> command.execute(
            version, "fabric", version.getName()));
    }

    @Test
    public void testGetCommand() {
        val command = new FabricCommand(LauncherMock.INSTANCE);
        val version = parseVersion(getJsonObject("version_parent.json"));
        assertEquals("1.19", version.getName());
        val java = new Java("dummyJava", 17);
        val jar = new File("dummyJar");

        val result = command.getCommand(version, java, jar);
        assertEquals(Arrays.asList(
            "dummyJava", "-jar", jar.getAbsolutePath(), "client",
            "-noprofile", "-mcversion", "1.19"), result);
    }

}
