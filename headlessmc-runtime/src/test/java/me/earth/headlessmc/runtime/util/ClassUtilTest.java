package me.earth.headlessmc.runtime.util;

import lombok.SneakyThrows;
import me.earth.headlessmc.runtime.reflection.ClassUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassUtilTest {
    @Test
    @SneakyThrows
    public void testClassUtil() {
        checkClass(boolean.class);
        checkClass(byte.class);
        checkClass(short.class);
        checkClass(int.class);
        checkClass(long.class);
        checkClass(float.class);
        checkClass(double.class);
        checkClass(char.class);
        checkClass(void.class);
        Assertions.assertThrows(ClassNotFoundException.class,
                                () -> checkClass(String.class));
        Assertions.assertThrows(ClassNotFoundException.class,
                                () -> ClassUtil.getPrimitiveClass(
                                    "unknown.class"));
    }

    private void checkClass(Class<?> clazz) throws ClassNotFoundException {
        Assertions.assertEquals(clazz,
                                ClassUtil.getPrimitiveClass(clazz.getName()));
    }

}