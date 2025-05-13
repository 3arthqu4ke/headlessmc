package me.earth.headlessmc.launcher.mods.files;

import lombok.Data;

import java.util.List;

@Data
public class ModFileReadResult {
    private final List<ModFile> mods;
    private final int nonModFiles;

}
