package io.github.headlesshq.headlessmc.launcher.launch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SystemPropertyHelperTest {
    @Test
    public void testSystemPropertyHelper() {
        String[] split = SystemPropertyHelper.splitSystemProperty("name=value");
        assertFalse(SystemPropertyHelper.isSystemProperty("name=value"));
        assertEquals(2, split.length);
        assertEquals("name", split[0]);
        assertEquals("value", split[1]);

        split = SystemPropertyHelper.splitSystemProperty("-Dname=value");
        assertTrue(SystemPropertyHelper.isSystemProperty("-Dname=value"));
        assertEquals(2, split.length);
        assertEquals("name", split[0]);
        assertEquals("value", split[1]);

        split = SystemPropertyHelper.splitSystemProperty("-Dname=va=lue");
        assertTrue(SystemPropertyHelper.isSystemProperty("-Dname=va=lue"));
        assertEquals(2, split.length);
        assertEquals("name", split[0]);
        assertEquals("va=lue", split[1]);

        split = SystemPropertyHelper.splitSystemProperty("-Dname=");
        assertTrue(SystemPropertyHelper.isSystemProperty("-Dname="));
        assertEquals(2, split.length);
        assertEquals("name", split[0]);
        assertEquals("", split[1]);

        split = SystemPropertyHelper.splitSystemProperty("-D=");
        assertTrue(SystemPropertyHelper.isSystemProperty("-D="));
        assertEquals(2, split.length);
        assertEquals("", split[0]);
        assertEquals("", split[1]);

        split = SystemPropertyHelper.splitSystemProperty("-D=value");
        assertTrue(SystemPropertyHelper.isSystemProperty("-D=value"));
        assertEquals(2, split.length);
        assertEquals("", split[0]);
        assertEquals("value", split[1]);

        assertThrows(IllegalArgumentException.class, () -> SystemPropertyHelper.splitSystemProperty("-D"));
        assertFalse(SystemPropertyHelper.isSystemProperty("-D"));
        assertThrows(IllegalArgumentException.class, () -> SystemPropertyHelper.splitSystemProperty("namevalue"));
        assertFalse(SystemPropertyHelper.isSystemProperty("namevalue"));
    }

    @Test
    public void testToSystemProperty() {
        assertEquals("-Dkey=value", SystemPropertyHelper.toSystemProperty("key", "value"));
    }

}
