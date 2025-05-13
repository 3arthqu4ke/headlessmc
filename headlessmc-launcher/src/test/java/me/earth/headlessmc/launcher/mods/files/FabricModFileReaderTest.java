package me.earth.headlessmc.launcher.mods.files;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class FabricModFileReaderTest {
    @Test
    public void testReadModFile() throws IOException {
        Path tempFile = ModFileReaderTest.createModFile("fabric.mod.json", "fabric.mod.json");
        FabricModFileReader reader = new FabricModFileReader();
        List<ModFile> mods = reader.read(() -> 0, tempFile);
        assertEquals(1, mods.size());
        ModFile mod = mods.get(0);
        assertEquals("fabric-api", mod.getName());
        assertEquals(0, mod.getId());
        assertEquals("Core API module providing key hooks and intercompatibility features.", mod.getDescription());
        assertEquals(1, mod.getAuthors().size());
        assertEquals("FabricMC", mod.getAuthors().get(0));
    }

}
