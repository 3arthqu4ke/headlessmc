package me.earth.headlessmc.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableTest {
    private static final String EXPECTED =
        "text     length\n" +
            "Test     4\n" +
            "Test2    5\n" +
            "Test3_   6";

    @Test
    public void testTable() {
        String actual = new Table<String>()
            .withColumn("text", s -> s)
            .withColumn("length", s -> String.valueOf(s.length()))
            .add("Test", "Test2", "Test3_")
            .build();

        assertEquals(EXPECTED, actual);

        String empty = new Table<String>()
            .withColumn("test", s -> s)
            .withColumn("test2", s -> s)
            .build();

        assertEquals("test   test2\n-      -", empty);

        String emptier = new Table<String>().build();
        assertEquals("", emptier);
    }

}
