package io.github.headlesshq.headlessmc.launcher.command;

import io.github.headlesshq.headlessmc.api.command.CommandContext;
import io.github.headlesshq.headlessmc.api.command.impl.CommandContextCommand;
import io.github.headlesshq.headlessmc.launcher.Launcher;

public class AbstractLauncherCommandCtxCommand extends CommandContextCommand {
    protected final Launcher ctx;

    public AbstractLauncherCommandCtxCommand(Launcher ctx, String name, String description, CommandContext commands) {
        super(ctx, name, description, commands);
        this.ctx = ctx;
    }

}
