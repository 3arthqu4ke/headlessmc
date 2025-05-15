package io.github.headlesshq.headlessmc.lwjgl.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RedirectionTest {
    @Test
    public void testOf() throws Throwable {
        assertEquals(0, Redirection.of(0).invoke(null, "", null));
    }

}
