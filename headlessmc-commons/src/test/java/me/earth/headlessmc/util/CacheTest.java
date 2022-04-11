package me.earth.headlessmc.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {
    @Test
    public void testCache() {
        // split this in multiple tests?
        AtomicReference<String> reference = new AtomicReference<>();
        Supplier<String> supplier = reference::get;

        Cache<String> cache = new Cache<>(supplier);
        assertFalse(cache.isPresent());
        assertNull(cache.get());

        String hello = "Hello";
        AtomicReference<String> ifPresent = new AtomicReference<>(hello);
        assertFalse(cache.ifPresent(ifPresent::set));
        assertEquals(hello, ifPresent.get());

        assertNull(cache.returnIfPresent(String::length));

        String expected = "Test";
        reference.set(expected);

        assertTrue(cache.isPresent());
        assertEquals(expected, cache.get());
        assertTrue(cache.ifPresent(ifPresent::set));
        assertEquals(expected, ifPresent.get());
        assertEquals(expected.length(), cache.returnIfPresent(String::length));
    }

}
