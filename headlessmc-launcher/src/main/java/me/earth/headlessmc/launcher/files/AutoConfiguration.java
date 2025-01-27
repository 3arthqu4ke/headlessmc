package me.earth.headlessmc.launcher.files;

import lombok.CustomLog;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.java.Java;
import me.earth.headlessmc.java.JavaScanner;
import me.earth.headlessmc.java.JavaVersionFinder;
import me.earth.headlessmc.java.JavaVersionParser;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.os.OS;
import me.earth.headlessmc.os.OSFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@CustomLog
public class AutoConfiguration {
    public static void runAutoConfiguration(FileManager fileManager) {
        runAutoConfiguration(fileManager, new JavaVersionFinder());
    }

    public static void runAutoConfiguration(FileManager fileManager, JavaVersionFinder javaVersionFinder) {
        Config dummyConfig = new ConfigImpl(new Properties(), "dummy", 0);
        if (dummyConfig.get(LauncherProperties.NO_AUTO_CONFIG, false)) {
            return;
        }

        if (!fileManager.get(false, false, "config.properties").exists()) {
            File file = fileManager.create("config.properties");
            OS os = OSFactory.detect(dummyConfig);
            List<Java> javas = javaVersionFinder.findJavaVersions(JavaScanner.of(new JavaVersionParser()), os);
            try (OutputStream output = Files.newOutputStream(file.toPath())) {
                output.write("hmc.java.versions=".getBytes(StandardCharsets.UTF_8));
                Iterator<Java> itr = javas.iterator();
                while (itr.hasNext()) {
                    Java java = itr.next();
                    // ignore javas in HeadlessMC/java, these get scanned anyway
                    if (Paths.get(java.getExecutable()).toAbsolutePath().startsWith(fileManager.getBase().toPath().toAbsolutePath())) {
                        continue;
                    }

                    output.write(java.getExecutable().replace("\\", "/").getBytes(StandardCharsets.UTF_8));
                    if (itr.hasNext()) {
                        output.write(";".getBytes(StandardCharsets.UTF_8));
                    }
                }
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

}
