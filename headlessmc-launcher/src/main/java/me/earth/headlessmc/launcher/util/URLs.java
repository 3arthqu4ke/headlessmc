package me.earth.headlessmc.launcher.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URL;

/**
 * Utility for {@link URL}s.
 */
@UtilityClass
public class URLs {
    @SneakyThrows
    public static URL url(String url) {
        return new URL(url);
    }

}
