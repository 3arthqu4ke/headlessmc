package me.earth.headlessmc.config;

import lombok.val;
import me.earth.headlessmc.api.config.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PropertyTypesTest {
    @Test
    public void testNumberProperty() {
        val number = PropertyTypes.number("test");
        Assertions.assertNull(number.parse(null));
        Assertions.assertEquals(1L, number.parse("1"));
        Assertions.assertThrows(NumberFormatException.class,
                                () -> number.parse("t"));
    }

    @Test
    public void testStringProperty() {
        val string = PropertyTypes.string("test");
        val expected = "test value";
        Assertions.assertNull(string.parse(null));
        Assertions.assertEquals(expected, string.parse(expected));
    }

    @Test
    public void testBooleanProperty() {
        val bool = PropertyTypes.bool("test");
        Assertions.assertNull(bool.parse(null));

        Assertions.assertTrue(bool.parse("true"));
        Assertions.assertTrue(bool.parse("True"));
        Assertions.assertTrue(bool.parse("TRUE"));
        Assertions.assertTrue(bool.parse("tRue"));

        Assertions.assertFalse(bool.parse("false"));
        Assertions.assertFalse(bool.parse("False"));
        Assertions.assertFalse(bool.parse("FALSE"));
        Assertions.assertFalse(bool.parse("faLse"));

        Assertions.assertFalse(bool.parse("test"));
    }

    @Test
    public void testArrayProperty() {
        Property<String[]> array = PropertyTypes.array("test", ";");
        Assertions.assertNull(array.parse(null));
        Assertions.assertNull(array.parse(""));

        Assertions.assertArrayEquals(new String[]{"test", "test"},
                                     array.parse("test;test"));
        Assertions.assertArrayEquals(new String[]{"test:test"},
                                     array.parse("test:test"));

        array = PropertyTypes.array("test", ":");
        Assertions.assertArrayEquals(new String[]{"test;test"},
                                     array.parse("test;test"));
        Assertions.assertArrayEquals(new String[]{"test", "test"},
                                     array.parse("test:test"));
    }

    @Test
    public void testConstant() {
        val constant = PropertyTypes.constant("test", SomeEnum.class);
        Assertions.assertEquals(SomeEnum.FIRST, constant.parse("first"));
        Assertions.assertEquals(SomeEnum.FIRST, constant.parse("First"));
        Assertions.assertEquals(SomeEnum.FIRST, constant.parse("FIRST"));
        Assertions.assertEquals(SomeEnum.FIRST, constant.parse("fiRst"));

        Assertions.assertEquals(SomeEnum.SECOND, constant.parse("second"));
        Assertions.assertEquals(SomeEnum.SECOND, constant.parse("Second"));
        Assertions.assertEquals(SomeEnum.SECOND, constant.parse("SECOND"));
        Assertions.assertEquals(SomeEnum.SECOND, constant.parse("seCond"));

        Assertions.assertNull(constant.parse(null));
        Assertions.assertNull(constant.parse("test"));
        Assertions.assertNull(constant.parse("this value doesn't exist"));
    }

    enum SomeEnum {
        FIRST,
        SECOND
    }

}
