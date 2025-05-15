package io.github.headlesshq.headlessmc.launcher.mods.modrinth;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
class ModrinthProject {
    @SerializedName("slug")
    private final String slug;
    @SerializedName("author")
    private final String author;
    @SerializedName("description")
    private final String description;

}
