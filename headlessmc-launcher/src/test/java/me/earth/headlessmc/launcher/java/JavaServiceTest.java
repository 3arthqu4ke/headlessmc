package me.earth.headlessmc.launcher.java;

import me.earth.headlessmc.api.config.ConfigImpl;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class JavaServiceTest {
    private final JavaService javaService = new JavaService(() -> new ConfigImpl(new Properties(), "dummy", 0));

    @Test
    public void testFindBestVersion() {
        javaService.clear();
        assertNull(javaService.findBestVersion(17));
        assertNull(javaService.findBestVersion(null));

        javaService.add(new Java("8", 8));
        javaService.add(new Java("17", 17));
        javaService.add(new Java("21", 21));

        assertEquals(8, Objects.requireNonNull(javaService.findBestVersion(8)).getVersion());
        assertEquals(17, Objects.requireNonNull(javaService.findBestVersion(17)).getVersion());
        assertEquals(21, Objects.requireNonNull(javaService.findBestVersion(21)).getVersion());

        assertEquals(8, Objects.requireNonNull(javaService.findBestVersion(7)).getVersion());
        assertEquals(17, Objects.requireNonNull(javaService.findBestVersion(9)).getVersion());
        assertEquals(21, Objects.requireNonNull(javaService.findBestVersion(19)).getVersion());

        assertEquals(8, Objects.requireNonNull(javaService.findBestVersion(null)).getVersion());

        assertNull(javaService.findBestVersion(23));
    }

    @Test
    public void testParseSystemProperty() {
        assertEquals(8, javaService.parseSystemProperty("1.8.0_152"));
        assertEquals(11, javaService.parseSystemProperty("11.0.2"));
        assertEquals(12, javaService.parseSystemProperty("12"));
        assertEquals(13, javaService.parseSystemProperty("13.0.1"));
    }

    @Test
    public void testGetCurrent() {
        assertNotNull(javaService.getCurrent());
    }

}
