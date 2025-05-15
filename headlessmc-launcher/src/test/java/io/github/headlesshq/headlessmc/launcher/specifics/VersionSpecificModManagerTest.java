package io.github.headlesshq.headlessmc.launcher.specifics;

import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.LauncherMock;
import io.github.headlesshq.headlessmc.launcher.download.DownloadService;
import io.github.headlesshq.headlessmc.launcher.files.FileManager;
import io.github.headlesshq.headlessmc.launcher.version.DummyVersion;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class VersionSpecificModManagerTest {
    @Test
    @Disabled("Downloads something from the internet")
    public void testVersionSpecificModManagerTest() throws VersionSpecificException, IOException {
        Launcher launcher = LauncherMock.create();
        launcher.getLauncherConfig().setFileManager(FileManager.forPath("build/test"));
        VersionSpecificModManager modManager = new VersionSpecificModManager(new DownloadService(), launcher.getLauncherConfig());
        modManager.addRepository(VersionSpecificMods.HMC_SPECIFICS);

        modManager.download(new DummyVersion("1.12.2-forge", new DummyVersion("1.12.2", null)), VersionSpecificMods.HMC_SPECIFICS);
    }

}
