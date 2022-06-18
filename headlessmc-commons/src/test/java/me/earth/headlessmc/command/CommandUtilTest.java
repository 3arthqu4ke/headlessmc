package me.earth.headlessmc.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandUtilTest {
    @Test
    public void testSplit() {
        Map<String, String[][]> c = new HashMap<>();
        c.put("test", new String[][]{{"test"}});
        c.put("test test", new String[][]{{"test", "test"}});
        c.put("test test test", new String[][]{{"test", "test", "test"}});
        c.put("test;test", new String[][]{{"test"}, {"test"}});
        c.put("test;test test", new String[][]{{"test"}, {"test", "test"}});
        c.put("test \"a b c\"", new String[][]{{"test", "a b c"}});
        c.put("test \"a \\\"b c\"", new String[][]{{"test", "a \"b c"}});
        c.put("test \\;", new String[][]{{"test", ";"}});
        // TODO: more test cases idk
        // TODO: "test \"a ;b c\"" -> new String[][]{{"test", "a "}, {"b c"}}
        // TODO: maybe remove semicolon?

        for (Map.Entry<String, String[][]> e : c.entrySet()) {
            String[][] split = CommandUtil.split(e.getKey());
            String[][] expected = e.getValue();
            System.err.println(Arrays.deepToString(split));
            Assertions.assertEquals(split.length, expected.length);
            for (int i = 0; i < split.length; i++) {
                Assertions.assertEquals(expected[i].length, split[i].length);
                // there could be a generic multi array equals :(
                Assertions.assertArrayEquals(split[i], expected[i]);
            }
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
