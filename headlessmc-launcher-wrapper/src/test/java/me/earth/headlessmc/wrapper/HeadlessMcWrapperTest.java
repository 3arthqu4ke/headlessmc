package me.earth.headlessmc.wrapper;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HeadlessMcWrapperTest {
    @Test
    public void testHeadlessMcWrapper() throws IOException {
        HeadlessMcWrapper.setClassLoader(null);
        assertNull(HeadlessMcWrapper.getClassLoader());
        try (URLClassLoader urlClassLoader = new URLClassLoader(new URL[0], null)) {
            HeadlessMcWrapper.setClassLoader(urlClassLoader);
            assertEquals(urlClassLoader, HeadlessMcWrapper.getClassLoader());
            HeadlessMcWrapper.setClassLoader(null);
        }
    }

}
