package me.earth.headlessmc.launcher.instrumentation.log4j;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.launcher.instrumentation.Target;
import me.earth.headlessmc.util.AbstractUtilityTest;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.JndiLookup;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static me.earth.headlessmc.launcher.instrumentation.InstrumentationTest.instrument;
import static org.junit.jupiter.api.Assertions.*;

public class Log4jPatchTests extends AbstractUtilityTest<Patchers> {
    @Test
    public void testMatches() {
        val transformer = Patchers.LOOKUP;
        val invalidTarget = new Target(false, "somePath");
        assertFalse(transformer.matches(invalidTarget));
        val validTarget = new Target(false, "log4j");
        assertTrue(transformer.matches(validTarget));
    }

    @Test
    @SneakyThrows
    public void testInstrumentInterpolator() {
        val transformer = Patchers.LOOKUP;
        Class<?> clazz = instrument(Interpolator.class, transformer);
        testClass(clazz, false);
    }

    @Test
    @SneakyThrows
    public void testInstrumentJndi() {
        val transformer = Patchers.JNDI;
        Class<?> clazz = instrument(JndiLookup.class, transformer);
        // unsafe is necessary because the jndi constructor throws an Exception.
        testClass(clazz, true);
    }

    @SneakyThrows
    private void testClass(Class<?> clazz, boolean unsafe) {
        val obj = unsafe
            ? initWithUnsafe(clazz)
            : clazz.getConstructor().newInstance();
        val result = clazz.getMethod("lookup", LogEvent.class, String.class)
                          .invoke(obj, null, "t");
        assertEquals("HeadlessMc prevented Log4j lookup!", result);
    }

    @SneakyThrows
    private Object initWithUnsafe(Class<?> clazz) {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe.allocateInstance(clazz);
    }

}