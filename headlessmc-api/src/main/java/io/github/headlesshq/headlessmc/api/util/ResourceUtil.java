package io.github.headlesshq.headlessmc.api.util;

import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.util.Objects;

@UtilityClass
public class ResourceUtil {
    public static InputStream getHmcResource(String name) {
        return getResource("headlessmc/" + name);
    }

    public static InputStream getResource(String name) {
        return Objects.requireNonNull(
            ResourceUtil.class.getClassLoader().getResourceAsStream(name),
            "Couldn't find Resource '" + name + "'");
    }

}
