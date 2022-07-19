package me.earth.headlessmc.util;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.*;

// this increases test coverage...
@SuppressWarnings("unused")
public abstract class AbstractUtilityTest<T> {
    @Test
    @SneakyThrows
    public void testPrivateCtr() {
        val type = getType();
        assertTrue(Modifier.isFinal(type.getModifiers()));
        for (Constructor<?> ctr : type.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(ctr.getModifiers()));
            assertEquals(0, ctr.getParameterTypes().length);
            ctr.setAccessible(true);
            val ex = assertThrows(InvocationTargetException.class,
                                  ctr::newInstance).getCause();
            assertInstanceOf(UnsupportedOperationException.class, ex);
        }
    }

    protected Class<?> getType() {
        // noinspection ConstantConditions
        Assumptions.assumeTrue(getClass() != AbstractUtilityTest.class);
        return ((Class<?>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0]);
    }

}
