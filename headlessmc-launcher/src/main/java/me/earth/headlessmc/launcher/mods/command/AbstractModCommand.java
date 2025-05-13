package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.launcher.mods.ModDistributionPlatformManager;

public abstract class AbstractModCommand extends AbstractLauncherCommand implements HasDistributionPlatformManager {
    public AbstractModCommand(Launcher ctx, String name, String desc) {
        super(ctx, name, desc);
    }

    @Override
    public ModDistributionPlatformManager getDistributionPlatformManager() {
        return ctx.getModManager().getModDistributionPlatformManager();
    }

}
