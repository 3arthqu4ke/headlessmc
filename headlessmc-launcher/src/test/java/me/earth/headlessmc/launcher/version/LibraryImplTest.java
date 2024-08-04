package me.earth.headlessmc.launcher.version;

import lombok.val;
import me.earth.headlessmc.launcher.os.OS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;

public class LibraryImplTest {
    @Test
    public void testGetPath() {
        val natives = new HashMap<String, String>();
        val _64 = new OS("windows", OS.Type.WINDOWS, "11", true);
        val _32 = new OS("windows", OS.Type.WINDOWS, "11", false);

        LibraryImpl lib = new LibraryImpl(
            natives, Extractor.NO_EXTRACTION, "test:test:test",
            Rule.ALLOW, "baseUrl", "url", "path", true);
        Assertions.assertEquals("path", lib.getPath(_64));

        lib = new LibraryImpl(
            natives, Extractor.NO_EXTRACTION, "test:test:test",
            Rule.ALLOW, "baseUrl", "url", "path-${arch}", true);
        Assertions.assertEquals("path-64", lib.getPath(_64));
        Assertions.assertEquals("path-32", lib.getPath(_32));

        lib = new LibraryImpl(
            natives, Extractor.NO_EXTRACTION, "test:test:test-${arch}",
            Rule.ALLOW, "baseUrl", "url", null, false);
        Assertions.assertEquals(
            String.join(File.separator, "test", "test",
                        "test-${arch}", "test-test-${arch}.jar"),
            lib.getPath(_64));

        lib = new LibraryImpl(
            natives, Extractor.NO_EXTRACTION, "test:test:test-${arch}",
            Rule.ALLOW, "baseUrl", "url", null, true);
        Assertions.assertEquals(
            String.join(File.separator, "test", "test",
                        "test-${arch}", "test-test-${arch}.jar"),
            lib.getPath(_64));

        natives.put(_32.getType().getName(), "natives-${arch}");
        Assertions.assertEquals(
            String.join(File.separator, "test", "test",
                        "test-${arch}", "test-test-${arch}-natives-32.jar"),
            lib.getPath(_32));
        Assertions.assertEquals(
            String.join(File.separator, "test", "test",
                        "test-${arch}", "test-test-${arch}-natives-64.jar"),
            lib.getPath(_64));
    }

}
