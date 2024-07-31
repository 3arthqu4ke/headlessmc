package me.earth.headlessmc.launcher.files;

import lombok.CustomLog;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.config.ConfigImpl;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.java.Java;
import me.earth.headlessmc.launcher.java.JavaService;
import me.earth.headlessmc.launcher.java.JavaVersionFinder;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.os.OSFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@CustomLog
public class AutoConfiguration {
    public static void runAutoConfiguration(FileManager fileManager) {
        Config dummyConfig = new ConfigImpl(new Properties(), "dummy", 0);
        if (dummyConfig.get(LauncherProperties.NO_AUTO_CONFIG, false)) {
            return;
        }

        if (!fileManager.get(false, false, "config.properties").exists()) {
            File file = fileManager.create("config.properties");
            OS os = OSFactory.detect(dummyConfig);
            JavaService javaService = new JavaService(() -> dummyConfig);
            List<Java> javas = JavaVersionFinder.findJavaVersions(javaService, os);
            try (OutputStream output = Files.newOutputStream(file.toPath())) {
                output.write("hmc.java.versions=".getBytes(StandardCharsets.UTF_8));
                Iterator<Java> itr = javas.iterator();
                while (itr.hasNext()) {
                    Java java = itr.next();
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
