package io.github.headlesshq.headlessmc.api;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ApiTest {
    @Test
    public void testApi() {
        HeadlessMcApi.setInstance(null);
        assertNull(HeadlessMcApi.getInstance());
        AtomicReference<HeadlessMc> listener = new AtomicReference<>();
        HeadlessMcApi.addListener(listener::set);
        assertNull(listener.get());
        HeadlessMc instance = new MockedHeadlessMc();
        HeadlessMcApi.setInstance(instance);
        assertEquals(instance, HeadlessMcApi.getInstance());
        assertEquals(instance, listener.get());
        AtomicReference<HeadlessMc> listener2 = new AtomicReference<>();
        HeadlessMcApi.addListener(listener2::set);
        assertEquals(instance, listener2.get());
    }

}
