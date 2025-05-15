package io.github.headlesshq.headlessmc.launcher;

import io.github.headlesshq.headlessmc.launcher.auth.AuthException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LauncherBuilderTest {
    @Test
    public void testLauncherBuilder() throws AuthException {
        LauncherBuilder launcherBuilder = new LauncherBuilder();
        Launcher launcher = launcherBuilder.buildDefault();
        assertNotNull(launcher);
    }

}
