package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.command.AbstractCommand;
import me.earth.headlessmc.launcher.Launcher;

public abstract class AbstractLauncherCommand extends AbstractCommand {
    protected final Launcher ctx;

    public AbstractLauncherCommand(Launcher ctx, String name, String desc) {
        super(ctx, name, desc);
        this.ctx = ctx;
    }

}
