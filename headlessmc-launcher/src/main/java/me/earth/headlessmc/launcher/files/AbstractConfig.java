package me.earth.headlessmc.launcher.files;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.config.Config;

@Getter
@RequiredArgsConstructor
public abstract class AbstractConfig implements Config {
    private final String name;
    private final int id;

}
