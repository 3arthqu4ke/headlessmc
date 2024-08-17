package me.earth.headlessmc.launcher.util;

import lombok.CustomLog;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URL;

/**
 * Utility for {@link URL}s.
 */
@CustomLog
@UtilityClass
public class URLs {
    /**
     * Creates a new {@link URL} for the given String.
     * Will throw a {@link java.net.MalformedURLException} if the URL is malformed.
     * This method is meant to reduce boilerplate for URLConstants by Sneaky Throwing this exception.
     *
     * @param url the string to create a URL for.
     * @return a URL for the String.
     */
    @SneakyThrows
    public static URL url(String url) {
        return new URL(url);
    }

}
