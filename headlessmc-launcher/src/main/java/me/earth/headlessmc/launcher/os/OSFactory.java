package me.earth.headlessmc.launcher.os;

import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.Property;
import me.earth.headlessmc.launcher.LauncherProperties;

import java.util.Locale;
import java.util.Objects;

@UtilityClass
// TODO: this, especially arch detection, can probably be improved
public class OSFactory {
    public static OS detect(Config config) {
        val name = getName(config);
        val version = getVersion(config);
        val type = getType(config, name);
        val arch = isArch(config, type);

        return new OS(name, type, version, arch);
    }

    private static String getName(Config config) {
        return getGeneric(config, LauncherProperties.OS_NAME, "os.name");
    }

    private static String getVersion(Config config) {
        return getGeneric(config, LauncherProperties.OS_VERSION, "os.version");
    }

    private static OS.Type getType(Config config, String os) {
        val type = config.get(LauncherProperties.OS_TYPE);
        if (type != null) {
            return OS.Type.valueOf(type.toUpperCase());
        }

        OS.Type result;
        if ((os = os.toLowerCase(Locale.ENGLISH)).contains("nux")
            || os.contains("solaris")
            || os.contains("nix")
            || os.contains("sunos")) {
            result = OS.Type.LINUX;
        } else if (os.contains("darwin") || os.contains("mac")) {
            result = OS.Type.OSX;
        } else if (os.contains("win")) {
            result = OS.Type.WINDOWS;
        } else {
            throw new IllegalStateException(
                "Couldn't detect your Operating System Type from '" + os
                    + "' please provide one of WINDOWS, OSX or LINUX with the "
                    + LauncherProperties.OS_TYPE + " property!");
        }

        return result;
    }

    private static boolean isArch(Config config, OS.Type type) {
        var arch = config.get(LauncherProperties.OS_ARCH);
        if (arch == null) {
            if (type == OS.Type.WINDOWS) {
                String p_arch = System.getenv("PROCESSOR_ARCHITECTURE");
                String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

                arch = p_arch != null && p_arch.endsWith("64")
                    || wow64Arch != null && wow64Arch.endsWith("64");
            } else {
                arch = Objects.requireNonNull(
                                  System.getProperty("os.arch"),
                                  "Couldn't detect if your OS uses 64 bit!")
                              .contains("64");
            }
        }

        return arch;
    }

    private static String getGeneric(Config config,
                                     Property<String> property,
                                     String systemProp) {
        var result = config.get(property);
        if (result == null) {
            result = System.getProperty(systemProp);
        }

        return Objects.requireNonNull(result, String.format(
            "Couldn't find %s, please provide it using the config!",
            property.getName()));
    }

}
