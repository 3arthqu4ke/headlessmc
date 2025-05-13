package me.earth.headlessmc.launcher.mods.files;

import me.earth.headlessmc.launcher.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ModFileReaderTest {
    public static Path createModFile(String entryName, String resource) throws IOException {
        Path tempFile = Files.createTempFile(resource.replace(".", "-"), ".jar");
        try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(tempFile))) {
            ZipEntry entry = new ZipEntry(entryName);
            out.putNextEntry(entry);
            try (InputStream in = FabricModFileReaderTest.class.getClassLoader().getResourceAsStream(resource)) {
                assertNotNull(in);
                IOUtil.copy(in, out);
            }

            out.closeEntry();
        }

        return tempFile;
    }
}
