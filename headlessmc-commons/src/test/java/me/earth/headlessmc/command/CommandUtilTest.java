package me.earth.headlessmc.command;

import me.earth.headlessmc.util.AbstractUtilityTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CommandUtilTest extends AbstractUtilityTest<CommandUtil> {
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
            assertArrayEquals(expected, split);
        }
    }

    @Test
    public void testLevenshtein() {
        String test = "test";
        String tset = "tset";
        String sett = "stte";
        String buzz = "buzz";

        assertEquals(2, CommandUtil.levenshtein(test, tset));
        assertEquals(4, CommandUtil.levenshtein(test, sett));
        assertEquals(4, CommandUtil.levenshtein(buzz, test));
    }

    @Test
    public void testHasArg() {
        String[] array = new String[]{"test", "-arg"};
        assertTrue(CommandUtil.hasFlag("-arg", array));
        assertTrue(CommandUtil.hasFlag("-ARg", array));
        assertFalse(CommandUtil.hasFlag("-wasd", array));
    }

    @Test
    public void testGetOption() {
        String[] array = new String[]{"--test", "option"};
        assertEquals("option", CommandUtil.getOption("--test", array));
        assertEquals("option", CommandUtil.getOption("--Test", array));
        assertNull(CommandUtil.getOption("--notThere"));
        assertNull(CommandUtil.getOption("--test", "--test"));
        assertNull(CommandUtil.getOption("--test"));
    }

}
