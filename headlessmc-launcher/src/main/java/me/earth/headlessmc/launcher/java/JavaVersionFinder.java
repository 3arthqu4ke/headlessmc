package me.earth.headlessmc.launcher.java;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import me.earth.headlessmc.launcher.os.OS;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@CustomLog
@UtilityClass
public class JavaVersionFinder {
    public static List<Java> findJavaVersions(JavaService javaService, OS os) {
        if (os.getType() == OS.Type.WINDOWS) {
            Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
            for (Path rootPath : rootDirectories) {
                Path javaDirPath = rootPath.resolve("Program Files").resolve("Java");
                if (Files.exists(javaDirPath) && Files.isDirectory(javaDirPath)) {
                    try (Stream<Path> stream = Files.list(javaDirPath)) {
                        List<Java> result = new ArrayList<>();
                        stream.forEach(javaVersion -> {
                            Path executable = javaVersion.resolve("bin").resolve("java.exe");
                            String path = executable.toAbsolutePath().toString();
                            if (path.toLowerCase(Locale.ENGLISH).endsWith(".exe")) {
                                path = path.substring(0, path.length() - 4);
                            }

                            Java java = javaService.scanJava(path);
                            if (java != null) {
                                result.add(java);
                            }
                        });

                        return result;
                    } catch (IOException e) {
                        log.error("Failed to list files in " + javaDirPath + " : " + e.getMessage());
                    }
                }
            }

            // search Users/<user>/.jdks?
        }

        return Collections.emptyList();
    }

}
