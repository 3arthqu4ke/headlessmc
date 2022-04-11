package me.earth.headlessmc.launcher.files;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.config.Config;

@RequiredArgsConstructor
public class DelegatingConfig implements Config {
    @Delegate
    private final Config config;

}