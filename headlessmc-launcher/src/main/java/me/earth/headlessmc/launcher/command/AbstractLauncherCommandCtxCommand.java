package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.impl.CommandContextCommand;
import me.earth.headlessmc.launcher.Launcher;

public class AbstractLauncherCommandCtxCommand extends CommandContextCommand {
    protected final Launcher ctx;

    public AbstractLauncherCommandCtxCommand(Launcher ctx, String name, String description, CommandContext commands) {
        super(ctx, name, description, commands);
        this.ctx = ctx;
    }

}
