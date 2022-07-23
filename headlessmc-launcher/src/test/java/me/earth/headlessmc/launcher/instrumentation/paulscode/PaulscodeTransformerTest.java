package me.earth.headlessmc.launcher.instrumentation.paulscode;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.launcher.instrumentation.InstrumentationTest;
import me.earth.headlessmc.launcher.instrumentation.Target;
import org.junit.jupiter.api.Test;
import paulscode.sound.Library;

import static org.junit.jupiter.api.Assertions.*;

public class PaulscodeTransformerTest extends InstrumentationTest {
    @Test
    public void testMatches() {
        val transformer = new PaulscodeTransformer();

        val invalidTarget = new Target(false, "test");
        assertFalse(transformer.matches(invalidTarget));

        val invalidTarget2 = new Target(false, "paulscode");
        assertFalse(transformer.matches(invalidTarget2));

        val validTarget = new Target(false, ".minecraft\\libraries\\com\\" +
            "paulscode\\soundsystem\\20120107\\soundsystem-20120107.jar\"");
        assertTrue(transformer.matches(validTarget));
    }

    @Test
    @SneakyThrows
    public void testPaulsCodeInstrumentation() {
        val transformer = new PaulscodeTransformer();
        val transformedClass = instrument(Library.class, transformer);
        testLibrary(transformedClass, true);
    }

    @Test
    public void testLibrary() {
        testLibrary(Library.class, false);
    }

    @SneakyThrows
    private void testLibrary(Class<?> clazz, boolean inv) {
        val obj = clazz.getConstructor().newInstance();
        val getter = clazz.getMethod("getMessage");

        clazz.getMethod("message", String.class).invoke(obj, "test");
        assertEquals(inv ? null : "test", getter.invoke(obj));

        clazz.getMethod("importantMessage", String.class).invoke(obj, "test1");
        assertEquals(inv ? null : "test1", getter.invoke(obj));

        clazz.getMethod("errorMessage", String.class).invoke(obj, "test2");
        assertEquals(inv ? null : "test2", getter.invoke(obj));

        clazz.getMethod("errorCheck", boolean.class, String.class)
             .invoke(obj, true, "test3");
        assertEquals(inv ? null : "test3", getter.invoke(obj));

        clazz.getMethod("printStackTrace", Exception.class)
             .invoke(obj, new Exception("test4"));
        assertEquals(inv ? null : "test4", getter.invoke(obj));
    }

}
