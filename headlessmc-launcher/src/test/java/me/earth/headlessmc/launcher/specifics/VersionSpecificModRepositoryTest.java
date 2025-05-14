package me.earth.headlessmc.launcher.specifics;

import me.earth.headlessmc.launcher.modlauncher.Modlauncher;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VersionSpecificModRepositoryTest {
    @Test
    public void testVersionSpecificModRepository() throws MalformedURLException {
        URL url = new URL("https://github.com/headlesshq/mc-runtime-test/releases/download/");
        VersionSpecificModRepository mcRuntimeTest = new VersionSpecificModRepository(url, "mc-runtime-test", "2.2.0", "-release");
        assertEquals("mc-runtime-test-1.21-2.2.0-neoforge-release.jar", mcRuntimeTest.getFileName(new VersionInfo("1.21", Modlauncher.NEOFORGE)));
        assertEquals("mc-runtime-test-1.19.2-2.2.0-fabric-release.jar", mcRuntimeTest.getFileName(new VersionInfo("1.19.2", Modlauncher.FABRIC)));
        URL expected = new URL("https://github.com/headlesshq/mc-runtime-test/releases/download/2.2.0/mc-runtime-test-1.12.2-2.2.0-lexforge-release.jar");
        assertEquals(expected, mcRuntimeTest.getDownloadURL(new VersionInfo("1.12.2", Modlauncher.LEXFORGE)));
    }

}
