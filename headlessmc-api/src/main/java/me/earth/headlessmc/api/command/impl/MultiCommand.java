package me.earth.headlessmc.api.command.impl;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.AbstractCommand;
import me.earth.headlessmc.api.command.CommandException;

public class MultiCommand extends AbstractCommand {
    public MultiCommand(HeadlessMc ctx) {
        super(ctx, "multi", "Run multiple commands together.");
        args.put("<command1 command2...>", "The commands to run.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        for (int i = 1; i < args.length; i++) {
            ctx.getCommandLine().getCommandContext().execute(args[i]);
        }
    }

}
