package me.earth.headlessmc.launcher.mods;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.mods.files.ModFileReaderManager;

@Getter
@RequiredArgsConstructor
public class ModManager {
    private final ModDistributionPlatformManager modDistributionPlatformManager;
    private final ModFileReaderManager modFileReaderManager;

    public static ModManager create(DownloadService downloadService) {
        return new ModManager(
                ModDistributionPlatformManager.create(downloadService),
                ModFileReaderManager.create()
        );
    }

}
