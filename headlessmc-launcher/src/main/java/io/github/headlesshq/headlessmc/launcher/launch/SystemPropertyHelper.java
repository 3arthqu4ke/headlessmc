package io.github.headlesshq.headlessmc.launcher.launch;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;

/**
 * Helps with parsing SystemProperties from their specification on the command line.
 * @see System#getProperties()
 */
// TODO: HANDLE QUOTES?! WHY IS THERE NO LIBRARY FOR THIS? Apache-Commons-CLI?
@CustomLog
@UtilityClass
public class SystemPropertyHelper {
    /**
     * Tests if a given string is in the form of "-Dkey=value".
     * TODO: This does not handle quotes right now.
     *
     * @param systemProperty the String to test.
     * @return {@code true} iff the given string is in the form of "-Dkey=value".
     */
    public static boolean isSystemProperty(String systemProperty) {
        return systemProperty.startsWith("-D") && systemProperty.contains("=");
    }

    /**
     * Splits a SystemProperty in the form of "-Dkey=value" into an array of key and value.
     * TODO: This does not handle quotes right now.
     *
     * @param systemProperty the SystemProperty in command line form.
     * @return the SystemProperty split in key and value.
     * @throws IllegalArgumentException if the String cannot be split into key and value, that is, if it does not contain a "=".
     */
    public static String[] splitSystemProperty(String systemProperty) {
        String keyValue = systemProperty;
        if (keyValue.startsWith("-D")) {
            keyValue = keyValue.substring(2);
        }

        String[] result = keyValue.split("=", 2);
        if (result.length != 2) {
            throw new IllegalArgumentException(systemProperty + " was not in the form of key=value!");
        }

        return result;
    }

    /**
     * Creates a String in the form of "-Dkey=value" from the given key and value.
     *
     * @param key the key.
     * @param value the value.
     * @return a String in the form of "-Dkey=value" from the given key and value.
     */
    public static String toSystemProperty(String key, String value) {
        return "-D" + key + "=" + value;
    }

}
