package io.github.headlesshq.headlessmc.runtime.commands.reflection;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.api.command.ParseUtil;
import io.github.headlesshq.headlessmc.runtime.reflection.RuntimeReflection;

public class CopyCommand extends AbstractRuntimeCommand {
    public CopyCommand(RuntimeReflection ctx) {
        super(ctx, "move", "Copies objects in memory.");
        args.put("<from>", "Address to copy the object from.");
        args.put("<to>", "Address to copy the object into.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException("Please specify two addresses!");
        }

        int from = ParseUtil.parseI(args[1]);
        int to = ParseUtil.parseI(args[2]);
        ctx.log("Moving " + from + " to " + to + ".");
        ctx.getVm().set(ctx.getVm().get(from), to);
    }

}
