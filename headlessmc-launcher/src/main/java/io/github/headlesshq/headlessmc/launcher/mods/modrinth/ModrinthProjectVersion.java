package io.github.headlesshq.headlessmc.launcher.mods.modrinth;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
class ModrinthProjectVersion {
    @SerializedName("game_versions")
    private final List<String> gameVersions;
    @SerializedName("loaders")
    private final List<String> loaders;
    @SerializedName("files")
    private final List<ModrinthFile> files;

}
