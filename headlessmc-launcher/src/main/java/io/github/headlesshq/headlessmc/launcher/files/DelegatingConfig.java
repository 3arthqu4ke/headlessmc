package io.github.headlesshq.headlessmc.launcher.files;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import io.github.headlesshq.headlessmc.api.config.Config;

@RequiredArgsConstructor
public class DelegatingConfig implements Config {
    @Delegate
    private final Config config;

}