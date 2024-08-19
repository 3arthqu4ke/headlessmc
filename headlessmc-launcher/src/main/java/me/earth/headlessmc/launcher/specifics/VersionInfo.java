package me.earth.headlessmc.launcher.specifics;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.modlauncher.Modlauncher;
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.launcher.version.family.FamilyUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Information about a Minecraft version.
 */
@Data
@RequiredArgsConstructor
public class VersionInfo {
    private final String version;
    private final @Nullable Modlauncher modlauncher;

    public VersionInfo(Version version) {
        Version vanilla = FamilyUtil.getOldestParent(version); // TODO: use parent name instead?
        this.version = vanilla.getName();
        this.modlauncher = Modlauncher.getFromVersionName(version.getName());
    }

    public String getDescription() {
        return version + (modlauncher == null ? "" : "-" + modlauncher.getHmcName());
    }

    public static VersionInfo requireModLauncher(Version version) throws VersionSpecificException {
        VersionInfo info = new VersionInfo(version);
        if (info.getModlauncher() == null) {
            throw new VersionSpecificException("Failed to find modlauncher for " + version.getName());
        }

        return info;
    }

}
