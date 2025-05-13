package me.earth.headlessmc.launcher.server;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.launcher.api.Platform;

@Data
public class ServerType implements HasName {
    private final Platform platform;
    @EqualsAndHashCode.Exclude
    private final ServerTypeDownloader downloader;

    @Override
    public String getName() {
        return platform.getName();
    }

}
