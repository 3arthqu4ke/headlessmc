package me.earth.headlessmc.launcher.specifics;

import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.version.DummyVersion;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class VersionSpecificModManagerTest {
    @Test
    @Disabled("Downloads something from the internet")
    public void testVersionSpecificModManagerTest() throws VersionSpecificException, IOException {
        VersionSpecificModManager modManager = new VersionSpecificModManager(new DownloadService(), FileManager.forPath("build/test"));
        modManager.addRepository(VersionSpecificMods.HMC_SPECIFICS);

        modManager.download(new DummyVersion("1.12.2-forge", new DummyVersion("1.12.2", null)), VersionSpecificMods.HMC_SPECIFICS);
    }

}
