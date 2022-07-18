package me.earth.headlessmc.command;

import me.earth.headlessmc.util.AbstractUtilityClassTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class CommandUtilTest extends AbstractUtilityClassTest {
    public CommandUtilTest() {
        super(CommandUtil.class);
    }

    @Test
    public void testSplit() {
        Map<String, String[]> c = new HashMap<>();
        c.put("test", new String[]{"test"});
        c.put("test test", new String[]{"test", "test"});
        c.put("test test test", new String[]{"test", "test", "test"});
        c.put("test \"a b c\"", new String[]{"test", "a b c"});
        c.put("test \"a \\\"b c\"", new String[]{"test", "a \"b c"});
        c.put("test \"a \\\"b c\" test",
              new String[]{"test", "a \"b c", "test"});
        c.put("test\\ test", new String[]{"test test"});

        for (Map.Entry<String, String[]> e : c.entrySet()) {
            String[] split = CommandUtil.split(e.getKey());
            String[] expected = e.getValue();
            Assertions.assertArrayEquals(expected, split);
        }
    }

    @Test
    public void testLevenshtein() {
        String test = "test";
        String tset = "tset";
        String sett = "stte";
        String buzz = "buzz";

        Assertions.assertEquals(2, CommandUtil.levenshtein(test, tset));
        Assertions.assertEquals(4, CommandUtil.levenshtein(test, sett));
        Assertions.assertEquals(4, CommandUtil.levenshtein(buzz, test));
    }

    @Test
    public void testHasArg() {
        String[] array = new String[]{"test", "-arg"};
        Assertions.assertTrue(CommandUtil.hasFlag("-arg", array));
        Assertions.assertTrue(CommandUtil.hasFlag("-ARg", array));
        Assertions.assertFalse(CommandUtil.hasFlag("-wasd", array));
    }

}
