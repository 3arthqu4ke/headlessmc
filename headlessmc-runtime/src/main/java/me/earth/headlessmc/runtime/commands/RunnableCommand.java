package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;

// TODO: Generic command to implement interface via Proxy + InvocationHandler?
public class RunnableCommand extends AbstractRuntimeCommand {
    public RunnableCommand(Runtime ctx) {
        super(ctx, "runnable", "Turns the given command into a runnable");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException("Please specify a command/address!");
        }

        int addr = ParseUtil.parseI(args[1]);
        Runnable runnable = () -> {
            if (!args[2].isEmpty()) {
                ctx.getCommandContext().execute(args[2]);
            }
        };

        ctx.getVm().set(runnable, addr);
        ctx.log("Created Runnable for '" + args[2] + "' at " + addr);
    }

}
