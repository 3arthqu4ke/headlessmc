package me.earth.headlessmc.launcher.files;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class FileUtil {
    public static void delete(File file) throws IOException {
        for (File content : listFiles(file)) {
            if (content.isDirectory()) {
                delete(content);
            } else {
                Files.delete(content.toPath());
            }
        }

        Files.delete(file.toPath());
    }

    public static File[] listFiles(File file) {
        val result = file.listFiles();
        return result == null ? new File[0] : result;
    }

}
