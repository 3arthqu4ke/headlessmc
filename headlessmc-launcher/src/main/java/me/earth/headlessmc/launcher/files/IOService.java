package me.earth.headlessmc.launcher.files;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOService {
    public void writeToFile(Path file, byte[] content) throws IOException {
        Files.createDirectories(file.getParent());
        try (OutputStream os = Files.newOutputStream(file)) {
            os.write(content);
        }
    }

}
