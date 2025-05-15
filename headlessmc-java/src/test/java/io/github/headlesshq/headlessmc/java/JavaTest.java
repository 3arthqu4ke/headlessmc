package io.github.headlesshq.headlessmc.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaTest {
    @Test
    public void testGetPath() {
        Java java = new Java("test", 8);
        assertEquals("test", java.getPath());

        java = new Java("test/bin/java", 8);
        assertEquals("test", java.getPath());

        java = new Java("test\\bin\\java", 8);
        assertEquals("test", java.getPath());

        java = new Java("test/bin/java.exe", 8);
        assertEquals("test", java.getPath());

        java = new Java("test\\bin\\java.exe", 8);
        assertEquals("test", java.getPath());
    }

}
