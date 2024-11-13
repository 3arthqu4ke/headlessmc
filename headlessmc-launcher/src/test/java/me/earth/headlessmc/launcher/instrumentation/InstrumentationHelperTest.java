package me.earth.headlessmc.launcher.instrumentation;

import me.earth.headlessmc.launcher.UsesResources;
import me.earth.headlessmc.launcher.launch.LaunchOptions;
import me.earth.headlessmc.launcher.version.ParsesVersions;
import me.earth.headlessmc.launcher.version.Version;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InstrumentationHelperTest implements UsesResources, ParsesVersions {
    @Test
    public void testAddXvfbTransformer() {
        Version version = parseVersion(getJsonObject("version_with_old_lwjgl.json"));
        LaunchOptions launchOptions = LaunchOptions.builder().version(version).build();
        List<Transformer> transformers = new ArrayList<>();
        InstrumentationHelper.addXvfbTransformer(launchOptions, transformers);
        assertEquals(1, transformers.size());

        version = parseVersion(getJsonObject("version_with_new_lwjgl.json"));
        launchOptions = LaunchOptions.builder().version(version).build();
        transformers = new ArrayList<>();
        InstrumentationHelper.addXvfbTransformer(launchOptions, transformers);
        assertEquals(0, transformers.size());
    }

}
