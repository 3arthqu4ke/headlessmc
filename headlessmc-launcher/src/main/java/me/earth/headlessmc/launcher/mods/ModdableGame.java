package me.earth.headlessmc.launcher.mods;

import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.launcher.api.Platform;
import me.earth.headlessmc.launcher.api.VersionId;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public interface ModdableGame extends HasName, HasId {
    Path getModsDirectory();

    String getVersionName();

    Platform getPlatform();

    @Nullable String getBuild();

    boolean isServer();

    default VersionId getVersionId() {
        return new VersionId(getPlatform(), getBuild(), isServer(), getVersionName());
    }

}
