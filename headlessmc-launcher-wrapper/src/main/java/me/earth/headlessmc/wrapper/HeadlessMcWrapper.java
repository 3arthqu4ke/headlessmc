package me.earth.headlessmc.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.net.URLClassLoader;

public class HeadlessMcWrapper {
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private static URLClassLoader classLoader;

}
