package io.github.headlesshq.headlessmc.launcher.mods.files;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaperModFileReaderTest {
    @Test
    public void testReadPlugin() throws IOException {
        Path tempFile = ModFileReaderTest.createModFile("plugin.yml", "plugin.yml");
        PaperModFileReader reader = PaperModFileReader.paper();
        List<ModFile> mods = reader.read(() -> 0, tempFile);
        assertEquals(1, mods.size());
        ModFile mod = mods.get(0);
        assertEquals("Paper-Test-Plugin", mod.getName());
        assertEquals(0, mod.getId());
        assertEquals("Paper Test Plugin", mod.getDescription());
        assertEquals(1, mod.getAuthors().size());
        assertEquals("PaperMC", mod.getAuthors().get(0));
    }

}
