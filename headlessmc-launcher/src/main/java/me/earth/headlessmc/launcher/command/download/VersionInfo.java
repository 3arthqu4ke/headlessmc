package me.earth.headlessmc.launcher.command.download;

import lombok.Data;
import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;

// TODO: @SerializedName
@Data
public class VersionInfo implements HasId, HasName {
    private final int id;
    private final String name;
    private final String type;
    private final String url;

}
