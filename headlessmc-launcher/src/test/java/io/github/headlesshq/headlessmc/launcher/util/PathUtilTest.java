package io.github.headlesshq.headlessmc.launcher.util;

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
    public void testStripQuotesNoQuotes() {
        String input = "/home/user/file.txt";
        Path expected = Paths.get(input);
        Path result = PathUtil.stripQuotes(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripQuotesAtStartAndEndNoQuotes() {
        String input = "/home/user/file.txt";
        String expected = input;
        String result = PathUtil.stripQuotesAtStartAndEnd(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripQuotesAtStartAndEndSingleQuoteStart() {
        String input = "\"/home/user/file.txt";
        String expected = input;
        String result = PathUtil.stripQuotesAtStartAndEnd(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripQuotesAtStartAndEndSingleQuoteEnd() {
        String input = "/home/user/file.txt\"";
        String expected = input;
        String result = PathUtil.stripQuotesAtStartAndEnd(input);

        assertEquals(expected, result);
    }

}
