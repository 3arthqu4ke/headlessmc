package me.earth.headlessmc.launcher.mods.files;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForgeModFileReaderTest {
    @Test
    public void testReadModFile() throws IOException {
        Path tempFile = ModFileReaderTest.createModFile("META-INF/mods.toml", "mods.toml");
        ForgeModFileReader reader = new ForgeModFileReader(false, "META-INF/mods.toml");
        List<ModFile> mods = reader.read(() -> 0, tempFile);
        assertEquals(1, mods.size());
        ModFile mod1 = mods.get(0);
        assertEquals("headlessmc", mod1.getName());
        assertEquals(0, mod1.getId());
        assertEquals("A Minecraft Launcher", mod1.getDescription());
        assertEquals(1, mod1.getAuthors().size());
        assertEquals("3arthqu4ke", mod1.getAuthors().get(0));
    }

    @Test
    public void testReadMcModInfoModFile() throws IOException {
        Path tempFile = ModFileReaderTest.createModFile("mcmod.info", "mcmod.info");
        ForgeModFileReader reader = new ForgeModFileReader(true);
        List<ModFile> mods = reader.read(() -> 0, tempFile);
        assertEquals(2, mods.size());
        ModFile mod1 = mods.get(0);
        assertEquals("examplemod", mod1.getName());
        assertEquals(0, mod1.getId());
        assertEquals("Example placeholder mod.", mod1.getDescription());
        assertEquals(1, mod1.getAuthors().size());
        assertEquals("ExampleDude", mod1.getAuthors().get(0));

        ModFile mod2 = mods.get(1);
        assertEquals("examplemod2", mod2.getName());
        assertEquals(0, mod2.getId());
        assertEquals("Example placeholder mod.2", mod2.getDescription());
        assertEquals(1, mod2.getAuthors().size());
        assertEquals("ExampleDude2", mod2.getAuthors().get(0));
    }

}
