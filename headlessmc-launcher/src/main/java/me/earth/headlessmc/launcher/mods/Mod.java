package me.earth.headlessmc.launcher.mods;

import lombok.Data;
import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.HasDescription;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class Mod implements HasId, HasName, HasDescription {
    private final String name;
    private final int id;
    private final @Nullable String description;
    private final List<String> authors;

    public String getDescription() {
        return description == null ? "" : description;
    }

}
