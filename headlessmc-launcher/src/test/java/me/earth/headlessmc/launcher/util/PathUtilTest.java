package me.earth.headlessmc.launcher.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathUtilTest {

    @Test
    public void testStripQuotes() {
        String input = "\"/home/user/file.txt\"";
        Path expected = Paths.get("/home/user/file.txt");
        Path result = PathUtil.stripQuotes(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripQuotes_noQuotes() {
        String input = "/home/user/file.txt";
        Path expected = Paths.get(input);
        Path result = PathUtil.stripQuotes(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripQuotesAtStartAndEnd_noQuotes() {
        String input = "/home/user/file.txt";
        String expected = input;
        String result = PathUtil.stripQuotesAtStartAndEnd(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripQuotesAtStartAndEnd_singleQuoteStart() {
        String input = "\"/home/user/file.txt";
        String expected = input;
        String result = PathUtil.stripQuotesAtStartAndEnd(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripQuotesAtStartAndEnd_singleQuoteEnd() {
        String input = "/home/user/file.txt\"";
        String expected = input;
        String result = PathUtil.stripQuotesAtStartAndEnd(input);

        assertEquals(expected, result);
    }

}
