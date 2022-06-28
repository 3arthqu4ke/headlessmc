package me.earth.headlessmc.lwjgl.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

public class DescriptionUtilTest {
    @Test
    public void testGetDesc_Method() throws NoSuchMethodException {
        Method method = DescriptionUtilTest.class.getDeclaredMethod(
            "testMethod", boolean.class, byte.class, short.class, int.class,
            long.class, double.class, float.class, char.class, String.class);
        String expected = method.getName() + Type.getMethodDescriptor(method);
        String actual = DescriptionUtil.getDesc(method);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetDesc_Class() {
        String expected = Type.getDescriptor(DescriptionUtilTest.class);
        String actual = DescriptionUtil.getDesc(DescriptionUtilTest.class);
        Assertions.assertEquals(expected, actual);
    }

    @SuppressWarnings("unused")
    private void testMethod(boolean b, byte by, short s, int i, long l,
                            double d, float f, char ch, String string) {
        throw new UnsupportedOperationException(
            "This method is used by the testGetDesc_Method test.");
    }

}
