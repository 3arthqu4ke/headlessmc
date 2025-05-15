package io.github.headlesshq.headlessmc.launcher.files;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.github.headlesshq.headlessmc.api.config.Config;

@Getter
@RequiredArgsConstructor
public abstract class AbstractConfig implements Config {
    private final String name;
    private final int id;

}
