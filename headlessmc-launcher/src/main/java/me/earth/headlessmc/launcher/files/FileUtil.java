package me.earth.headlessmc.launcher.files;

import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.launcher.util.IOConsumer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class FileUtil {
    public static void delete(File file) throws IOException {
        iterate(file, f -> Files.delete(f.toPath()));
    }

    public static void iterate(File file, IOConsumer<File> action)
        throws IOException {
        for (File content : listFiles(file)) {
            if (content.isDirectory()) {
                iterate(content, action);
            } else {
                action.accept(content);
            }
        }

        action.accept(file);
    }

    public static File[] listFiles(File file) {
        val result = file.listFiles();
        return result == null ? new File[0] : result;
    }

}
