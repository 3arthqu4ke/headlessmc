package me.earth.headlessmc.launcher.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionUtilTest {
    @Test
    public void testIsOlderThan() {
        assertTrue(VersionUtil.isOlderThan("1.12.2", "1.21.1"));
        assertTrue(VersionUtil.isOlderThan("1.11", "2.0.0"));
        assertTrue(VersionUtil.isOlderThanSafe("1.12.1", "1.12.2"));
        assertFalse(VersionUtil.isOlderThan("1.2.1", "1.1.1"));
        assertFalse(VersionUtil.isOlderThan("2.0.0", "1.1.1"));
        assertFalse(VersionUtil.isOlderThanSafe("1.1.2", "1.1.1"));

        assertFalse(VersionUtil.isOlderThanSafe("1.15", "1.15.0"));
        assertFalse(VersionUtil.isOlderThanSafe("1.15.0", "1.15"));

        assertFalse(VersionUtil.isOlderThanSafe("1.15", "1.14.4"));
        assertTrue(VersionUtil.isOlderThanSafe("1.14.4", "1.15"));
    }

}
