package me.earth.headlessmc.testplugin;

import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.plugin.HeadlessMcPlugin;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPlugin implements HeadlessMcPlugin {
    @Override
    public String getName() {
        return "Test";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void init(Launcher launcher) {
        launcher.getExitManager().setExitManager(i -> System.out.println("Test plugin active, exited with code " + i));

        assertThrows(ClassNotFoundException.class, () -> Class.forName("me.earth.headlessmc.testplugin.DummyClassThatCantBeLoaded"));
    }

}
