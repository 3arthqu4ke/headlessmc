package io.github.headlesshq.headlessmc.launcher.mods.command;

import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.command.AbstractLauncherCommand;
import io.github.headlesshq.headlessmc.launcher.mods.ModDistributionPlatformManager;

public abstract class AbstractModCommand extends AbstractLauncherCommand implements HasDistributionPlatformManager {
    public AbstractModCommand(Launcher ctx, String name, String desc) {
        super(ctx, name, desc);
    }

    @Override
    public ModDistributionPlatformManager getDistributionPlatformManager() {
        return ctx.getModManager().getModDistributionPlatformManager();
    }

}
