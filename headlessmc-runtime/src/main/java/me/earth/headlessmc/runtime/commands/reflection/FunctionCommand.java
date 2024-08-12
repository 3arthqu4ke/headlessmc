package me.earth.headlessmc.runtime.commands.reflection;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.ParseUtil;
import me.earth.headlessmc.runtime.reflection.RuntimeReflection;
import me.earth.headlessmc.runtime.reflection.SegmentationFault;

import java.util.function.Function;

public class FunctionCommand extends AbstractRuntimeCommand {
    public FunctionCommand(RuntimeReflection ctx) {
        super(ctx, "function", "Turns the given command into a function.");
        args.put("<cmd>", "The command to be executed on apply.");
        args.put("<addr>", "Where to store the function to.");
        args.put("<in>", "Address to store the input at.");
        args.put("<out>", "Address to return the output from.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length < 5) {
            throw new CommandException(
                "Please specify a command/address/from/to!");
        }

        int addr = ParseUtil.parseI(args[2]);
        int from = ParseUtil.parseI(args[3]);
        ctx.getVm().get(from);
        int to = ParseUtil.parseI(args[4]);
        ctx.getVm().checkSegfault(to);
        Function<?, ?> function = input -> {
            try {
                ctx.getVm().set(input, from);
                if (!args[1].isEmpty()) {
                    ctx.getCommandLine().getCommandContext().execute(args[1]);
                }
                return ctx.getVm().get(to);
            } catch (SegmentationFault e) {
                throw new IllegalStateException("Has the VM size changed?", e);
            }
        };

        ctx.getVm().set(function, addr);
        ctx.log("Created Function for '" + args[1] + "' at " + addr);
    }

}
