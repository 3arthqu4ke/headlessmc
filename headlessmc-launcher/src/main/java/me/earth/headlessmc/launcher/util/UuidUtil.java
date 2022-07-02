package me.earth.headlessmc.launcher.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class UuidUtil {
    private static final Pattern PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}" +
            "\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$");

    public static boolean isUuid(String string) {
        return PATTERN.matcher(string).find();
    }

}
