package io.github.headlesshq.headlessmc.launcher.mods;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import io.github.headlesshq.headlessmc.launcher.download.DownloadService;
import io.github.headlesshq.headlessmc.launcher.mods.files.ModFileReaderManager;

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
