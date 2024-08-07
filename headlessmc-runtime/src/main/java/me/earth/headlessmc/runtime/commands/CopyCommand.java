package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;

public class CopyCommand extends AbstractRuntimeCommand {
    public CopyCommand(Runtime ctx) {
        super(ctx, "move", "Copies objects in memory.");
        args.put("<from>", "Address to copy the object from.");
        args.put("<to>", "Address to copy the object into.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException("Please specify two addresses!");
        }

        int from = ParseUtil.parseI(args[1]);
        int to = ParseUtil.parseI(args[2]);
        ctx.log("Moving " + from + " to " + to + ".");
        ctx.getVm().set(ctx.getVm().get(from), to);
    }

}
