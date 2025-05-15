package io.github.headlesshq.headlessmc.launcher.files;

import lombok.experimental.UtilityClass;
import io.github.headlesshq.headlessmc.api.config.Config;
import io.github.headlesshq.headlessmc.launcher.LauncherProperties;
import io.github.headlesshq.headlessmc.os.OS;

import java.io.File;

@UtilityClass
public class MinecraftFinder {
    public static FileManager find(Config config, OS os) {
        String dir = config.get(LauncherProperties.MC_DIR);
        if (dir == null) {
            switch (os.getType()) {
                case WINDOWS:
                    dir = String.format("%s%s.minecraft%s",
                                        System.getenv("APPDATA"),
                                        File.separator, File.separator);
                    break;
                case LINUX:
                    dir = System.getProperty("user.home") + "/.minecraft/";
                    break;
                case OSX:
                    dir = String.format(
                        "%s/Library/Application Support/minecraft/",
                        System.getProperty("user.home"));
                    break;
                default:
                    throw new IllegalStateException(os.getType().toString());
            }
        }

        return FileManager.mkdir(dir);
    }

}
