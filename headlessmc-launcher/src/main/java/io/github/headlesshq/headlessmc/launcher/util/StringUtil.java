package io.github.headlesshq.headlessmc.launcher.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
    public static String cutOfEnd(String string, int toCutOf) {
        if (toCutOf < 0) {
            throw new IllegalArgumentException(
                toCutOf + " was smaller than 0 while cutting '" + string + "'");
        }

        if (string.length() - toCutOf < 0) {
            throw new IllegalArgumentException(
                toCutOf + " was to big long for cutting '" + string + "'");
        }

        return string.substring(0, string.length() - toCutOf);
    }

}
