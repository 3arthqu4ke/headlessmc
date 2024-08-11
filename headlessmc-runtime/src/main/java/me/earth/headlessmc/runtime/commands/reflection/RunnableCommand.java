package me.earth.headlessmc.runtime.commands.reflection;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.ParseUtil;
import me.earth.headlessmc.runtime.reflection.RuntimeReflection;

// TODO: Generic command to implement interface via Proxy + InvocationHandler?
public class RunnableCommand extends AbstractRuntimeCommand {
    public RunnableCommand(RuntimeReflection ctx) {
        super(ctx, "runnable", "Turns the given command into a runnable");
        args.put("<addr>", "The address to store the runnable in.");
        args.put("<args>", "One or multiple commands to run.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException("Please specify a command/address!");
        }

        int addr = ParseUtil.parseI(args[1]);
        Runnable runnable = () -> {
            for (int i = 2; i < args.length; i++) {
                ctx.getCommandLine().getCommandContext().execute(args[i]);
            }
        };

        ctx.getVm().set(runnable, addr);
        ctx.log("Created Runnable at " + addr);
    }

}
