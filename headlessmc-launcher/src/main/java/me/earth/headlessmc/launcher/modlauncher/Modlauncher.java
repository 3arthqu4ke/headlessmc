package me.earth.headlessmc.launcher.modlauncher;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum Modlauncher {
    FABRIC("fabric", "fabric"),
    LEXFORGE("forge", "lexforge"),
    NEOFORGE("neoforge", "neoforge");

    private final String officialName;
    private final String hmcName;

    public static @Nullable Modlauncher getFromVersionName(String name) {
        String lower = name.toLowerCase(Locale.ENGLISH);
        if (lower.contains(FABRIC.officialName)) {
            return FABRIC;
        }
        // ask for neoforge before forge
        if (lower.contains(NEOFORGE.officialName)) {
            return NEOFORGE;
        }

        if (lower.contains(LEXFORGE.officialName)) {
            return LEXFORGE;
        }

        return null;
    }

}
