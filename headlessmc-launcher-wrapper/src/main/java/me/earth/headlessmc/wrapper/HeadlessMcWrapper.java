package me.earth.headlessmc.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.wrapper.plugin.TransformingClassloader;

public class HeadlessMcWrapper {
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private static TransformingClassloader classLoader;

}
