package me.earth.headlessmc.launcher.version;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class VersionExecutable {
    private final String url;
    private final @Nullable String sha1;
    private final @Nullable Long size;

}
