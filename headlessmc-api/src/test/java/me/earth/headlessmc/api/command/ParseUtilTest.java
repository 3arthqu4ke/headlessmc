package me.earth.headlessmc.api.command;

import lombok.SneakyThrows;
import me.earth.headlessmc.api.util.AbstractUtilityTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParseUtilTest extends AbstractUtilityTest<ParseUtil> {
    @Test
    @SneakyThrows
    public void testParseLong() {
        Assertions.assertThrows(CommandException.class,
                                () -> ParseUtil.parseL(null, 0, 1000));
        Assertions.assertThrows(CommandException.class,
                                () -> ParseUtil.parseL("test", 0, 1000));
        Assertions.assertThrows(CommandException.class,
                                () -> ParseUtil.parseL("1001", 0, 1000));
        Assertions.assertEquals(1000L, ParseUtil.parseL("1000", 0, 1000));

        Assertions.assertThrows(CommandException.class, () -> ParseUtil.parseL("1000", 1001, 1002));
    }

    @Test
    @SneakyThrows
    public void testParseDouble() {
        Assertions.assertThrows(CommandException.class,
                                () -> ParseUtil.parseD(null, 0, 1000));
        Assertions.assertThrows(CommandException.class,
                                () -> ParseUtil.parseD("test", 0, 1000));
        Assertions.assertThrows(CommandException.class,
                                () -> ParseUtil.parseD("1001", 0, 1000));
        Assertions.assertEquals(1000.0, ParseUtil.parseD("1000", 0, 1000));
        Assertions.assertEquals(999.99, ParseUtil.parseD("999.99", 0, 1000));
        Assertions.assertThrows(CommandException.class, () -> ParseUtil.parseD("1000", 1001, 1002));
    }

}
