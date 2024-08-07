package me.earth.headlessmc.wrapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.wrapper.plugin.TransformingClassloader;
import me.earth.headlessmc.wrapper.plugin.TransformingPluginFinder;

import java.nio.file.Path;
import java.util.function.Function;

public class HeadlessMcWrapper {
    @Setter
    @Getter
    private static Function<Path, TransformingPluginFinder> pluginFinderFactory = TransformingPluginFinder::new;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private static TransformingClassloader classLoader;

}
