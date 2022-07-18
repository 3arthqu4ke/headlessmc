package me.earth.headlessmc.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

// this increases test coverage...
@RequiredArgsConstructor
public abstract class AbstractUtilityClassTest {
    private final Class<?> type;

    @Test
    @SneakyThrows
    public void testPrivateCtr() {
        for (Constructor<?> ctr : type.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(ctr.getModifiers()));
            assertEquals(0, ctr.getParameterTypes().length);
            ctr.setAccessible(true);
            val ex = assertThrows(InvocationTargetException.class,
                                  ctr::newInstance).getCause();
            assertInstanceOf(UnsupportedOperationException.class, ex);
        }
    }

}
