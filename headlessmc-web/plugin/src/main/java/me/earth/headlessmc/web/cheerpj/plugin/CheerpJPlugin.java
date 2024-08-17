package me.earth.headlessmc.web.cheerpj.plugin;

import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.plugin.HeadlessMcPlugin;

public class CheerpJPlugin implements HeadlessMcPlugin {
    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void init(Launcher launcher) {
        // NOP
    }

    @Override
    public String getName() {
        return "CheerpJ";
    }

    @Override
    public String getDescription() {
        return "Makes HeadlessMc run in your browser.";
    }

}
