package io.github.headlesshq.headlessmc.api.util;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAbstractUtilityTest
    extends AbstractUtilityTest<TestAbstractUtilityTest.SomeUtilityClass> {

    @Test
    public void test() {
        assertEquals(SomeUtilityClass.class, getType());
    }

    @UtilityClass
    public static final class SomeUtilityClass {
    }

}
