package me.earth.headlessmc.launcher.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JavaVersionParserTest {
    private final JavaVersionParser parser = new JavaVersionParser();

    @Test
    public void testParse() throws IOException {
        int version = parser.parseVersion(
            "java version \"18.0.1.1\" 2022-04-22\nJava(TM) SE Runtime" +
                " Environment (build 18.0.1.1+2-6)\nJava HotSpot(TM)" +
                " 64-Bit Server VM (build 18.0.1.1+2-6, mixed mode, sharing)");
        Assertions.assertEquals(18, version);

        version = parser.parseVersion(
            "java version \"17.0.2\" 2022-01-18 LTS\nJava(TM) SE" +
                " Runtime Environment (build 17.0.2+8-LTS-86)\n" +
                "Java HotSpot(TM) 64-Bit Server VM (build 17.0.2+8-LTS-86," +
                " mixed mode, sharing)");
        Assertions.assertEquals(17, version);

        version = parser.parseVersion(
            "java version \"1.8.0_331\"\nJava(TM) SE" +
                " Runtime Environment (build 1.8.0_331-b09)\n" +
                "Java HotSpot(TM) 64-Bit Server VM (build 25.331-b09," +
                " mixed mode)");
        Assertions.assertEquals(8, version);

        version = parser.parseVersion(
            "java version \"17-internal\" 2021-09-14\nOpenJDK" +
                " Runtime Environment (build 17-internal+0-adhoc..src)\n" +
                "OpenJDK 64-Bit Server VM (build 17-" +
                "internal+0-adhoc..src)");
        Assertions.assertEquals(17, version);

        Assertions.assertThrows(IOException.class,
                                () -> parser.parseVersion("test"));
    }

}
