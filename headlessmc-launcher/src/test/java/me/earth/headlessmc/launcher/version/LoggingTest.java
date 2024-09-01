package me.earth.headlessmc.launcher.version;

import lombok.val;
import me.earth.headlessmc.launcher.UsesResources;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoggingTest implements UsesResources {
    @Test
    public void testParse() throws MalformedURLException {
        val element = getJsonObject("logging.json");
        Logging logging = Logging.getFromVersion(element);
        assertNotNull(logging);
        assertEquals("-Dlog4j.configurationFile=${path}", logging.getArgument());
        assertEquals("log4j2-xml", logging.getType());
        Logging.File file = logging.getFile();
        assertEquals("client-1.12.xml", file.getId());
        assertEquals("bd65e7d2e3c237be76cfbef4c2405033d7f91521", file.getSha1());
        assertEquals(888, file.getSize());
        assertEquals(new URL("https://piston-data.mojang.com/v1/objects/bd65e7d2e3c237be76cfbef4c2405033d7f91521/client-1.12.xml"), file.getUrl());
    }

}
