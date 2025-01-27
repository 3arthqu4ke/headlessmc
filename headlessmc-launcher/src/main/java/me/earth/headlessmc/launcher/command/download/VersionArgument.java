package me.earth.headlessmc.launcher.command.download;

import lombok.Data;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.modlauncher.Modlauncher;
import org.jetbrains.annotations.Nullable;

@Data
public class VersionArgument implements HasName {
    private final @Nullable Modlauncher modlauncher;
    private final String name;

    public static VersionArgument parseVersion(String version) throws CommandException {
        String[] split = version.split(":"); // TODO: allow escaped :?
        if (split.length == 1) {
            return new VersionArgument(null, version);
        } else if (split.length == 2) {
            Modlauncher modlauncher = Modlauncher.getFromVersionName(split[0]);
            if (modlauncher == null) {
                throw new CommandException("Failed to parse Modlauncher (fabric, neoforge, forge) from: " + split[0]);
            }

            return new VersionArgument(modlauncher, split[1]);
        } else {
            throw new CommandException("To many ':' specified, expected format <version> or <modloader>:<version>, but was: " + version);
        }
    }

}
