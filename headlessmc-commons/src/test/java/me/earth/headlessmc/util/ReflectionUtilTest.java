package me.earth.headlessmc.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReflectionUtilTest {
    @Test
    public void testReflectionUtil_iterate() {
        Class<?> clazz = ReflectionUtil.iterate(SubClass.class, c -> c);
        Assertions.assertEquals(SubClass.class, clazz);

        clazz = ReflectionUtil.iterate(SubClass.class, c -> null);
        Assertions.assertNull(clazz);

        boolean[] found = new boolean[]{false};
        clazz = ReflectionUtil.iterate(SubClass.class, c -> {
            if (found[0]) {
                return c;
            }
            found[0] = true;
            return null;
        });
        Assertions.assertEquals(SuperClass.class, clazz);
    }

    @Test
    public void test_collect() {
        SubClass object = new SubClass();
        List<String> collected = ReflectionUtil.collect(object, String.class);
        Assertions.assertEquals(2, collected.size());
        Assertions.assertEquals(object.subClassString, collected.get(0));
        Assertions.assertEquals(object.superClassString, collected.get(1));
    }

    private static class SuperClass {
        public final String superClassString = "test";
    }

    private static final class SubClass extends SuperClass {
        public final String subClassString = "subClassString";
    }

}
