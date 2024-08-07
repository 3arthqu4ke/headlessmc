package me.earth.headlessmc.launcher.os;

import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.launcher.LauncherProperties;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OSFactoryTest {
    @Test
    public void testOSFactory() {
        Properties properties = new Properties();
        properties.put(LauncherProperties.OS_ARCH.getName(), "true");
        properties.put(LauncherProperties.OS_NAME.getName(), "win");
        properties.put(LauncherProperties.OS_TYPE.getName(), "WINDOWS");
        properties.put(LauncherProperties.OS_VERSION.getName(), "10");
        Config config = new ConfigImpl(properties, "test", 0);

        OS os = OSFactory.detect(config);
        assertEquals("win", os.getName());
        assertEquals(OS.Type.WINDOWS, os.getType());
        assertEquals("10", os.getVersion());
        assertTrue(os.isArch());
    }

}
