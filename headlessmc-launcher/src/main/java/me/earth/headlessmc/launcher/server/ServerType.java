package me.earth.headlessmc.launcher.server;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.earth.headlessmc.api.HasName;

@Data
public class ServerType implements HasName {
    private final String name;
    @EqualsAndHashCode.Exclude
    private final ServerTypeDownloader downloader;

}
