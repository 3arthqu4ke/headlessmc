package me.earth.headlessmc.launcher.java;

import lombok.CustomLog;
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
public class JavaVersionFinder {
    public List<Java> findJavaVersions(JavaService javaService, OS os) {
        List<Java> result = new ArrayList<>();
        if (os.getType() == OS.Type.WINDOWS) {
            Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
            for (Path rootPath : rootDirectories) {
                result.addAll(checkDirectory(javaService, rootPath.resolve("Program Files").resolve("Java"), os));
            }

            // search Users/<user>/.jdks?
        } else if (os.getType() == OS.Type.LINUX) {
            Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
            for (Path rootPath : rootDirectories) {
                result.addAll(checkDirectory(javaService, rootPath.resolve("usr").resolve("lib").resolve("jvm"), os));
                result.addAll(checkDirectory(javaService, rootPath.resolve("usr").resolve("local"), os));
            }
        }

        return result;
    }

    private List<Java> checkDirectory(JavaService javaService, Path javaDirPath, OS os) {
        if (Files.exists(javaDirPath) && Files.isDirectory(javaDirPath)) {
            try (Stream<Path> stream = Files.list(javaDirPath)) {
                List<Java> result = new ArrayList<>();
                stream.forEach(javaVersion -> {
                    Path executable = javaVersion.resolve("bin").resolve(os.getType() == OS.Type.WINDOWS ? "java.exe" : "java");
                    if (Files.exists(executable) && !Files.isDirectory(executable)) {
                        String path = executable.toAbsolutePath().toString();
                        if (path.toLowerCase(Locale.ENGLISH).endsWith(".exe")) {
                            path = path.substring(0, path.length() - 4);
                        }

                        Java java = javaService.scanJava(path);
                        if (java != null) {
                            result.add(java);
                        }
                    }
                });

                return result;
            } catch (IOException e) {
                log.error("Failed to list files in " + javaDirPath + " : " + e.getMessage());
            }
        }

        return Collections.emptyList();
    }

}
