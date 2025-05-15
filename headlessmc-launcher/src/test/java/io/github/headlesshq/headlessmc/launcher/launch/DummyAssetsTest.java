package io.github.headlesshq.headlessmc.launcher.launch;

import io.github.headlesshq.headlessmc.launcher.download.DummyAssets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DummyAssetsTest {
    @Test
    public void testGetFileEnding() {
        DummyAssets dummyAssets = new DummyAssets();
        assertEquals("ogg", dummyAssets.getFileEnding("test.ogg"));
        assertEquals("json", dummyAssets.getFileEnding("test.json"));
        assertEquals("", dummyAssets.getFileEnding("test"));
        assertEquals("png", dummyAssets.getFileEnding("test.test.test.png"));

        assertNotNull(dummyAssets.getResource("test.OGG"));
        assertNotNull(dummyAssets.getResource("test.pNg"));
        assertNotNull(dummyAssets.getResource("test.png"));

        assertNull(dummyAssets.getResource("test.notInThere"));
    }

}
