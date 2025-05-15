package io.github.headlesshq.headlessmc.launcher.command.download;

import lombok.Data;
import io.github.headlesshq.headlessmc.api.HasId;
import io.github.headlesshq.headlessmc.api.HasName;

// TODO: @SerializedName
@Data
public class VersionInfo implements HasId, HasName {
    private final int id;
    private final String name;
    private final String type;
    private final String url;

}
