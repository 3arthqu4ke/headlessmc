package me.earth.headlessmc.launcher.mods;

import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.mods.modrinth.Modrinth;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModDistributionPlatformManager implements Iterable<ModDistributionPlatform> {
    private final List<ModDistributionPlatform> platforms = new ArrayList<>();

    @Override
    public @NotNull Iterator<ModDistributionPlatform> iterator() {
        return platforms.iterator();
    }

    public static ModDistributionPlatformManager create(DownloadService downloadService) {
        ModDistributionPlatformManager manager = new ModDistributionPlatformManager();
        manager.platforms.add(new Modrinth(downloadService));
        return manager;
    }

}
