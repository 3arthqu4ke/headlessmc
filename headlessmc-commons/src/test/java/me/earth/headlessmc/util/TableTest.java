package me.earth.headlessmc.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

        Assertions.assertEquals(EXPECTED, actual);
    }

}
