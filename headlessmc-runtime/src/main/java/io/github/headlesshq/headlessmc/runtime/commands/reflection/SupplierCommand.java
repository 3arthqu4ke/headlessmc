package io.github.headlesshq.headlessmc.runtime.commands.reflection;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.api.command.ParseUtil;
import io.github.headlesshq.headlessmc.runtime.reflection.RuntimeReflection;
import io.github.headlesshq.headlessmc.runtime.reflection.SegmentationFault;

import java.util.function.Supplier;

public class SupplierCommand extends AbstractRuntimeCommand {
    public SupplierCommand(RuntimeReflection ctx) {
        super(ctx, "supplier", "Turns the given command into a supplier.");
        args.put("<cmd>", "The command to execute when 'get' is called.");
        args.put("<addr>", "The address to store the supplier into.");
        args.put("<result>", "The address to return the result of 'get' from.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length < 4) {
            throw new CommandException(
                "Please specify a command/address/from!");
        }

        int addr = ParseUtil.parseI(args[2]);
        int to = ParseUtil.parseI(args[3]);
        ctx.getVm().checkSegfault(to);
        Supplier<?> supplier = () -> {
            try {
                if (!args[1].isEmpty()) {
                    ctx.getCommandLine().getCommandContext().execute(args[1]);
                }
                return ctx.getVm().get(to);
            } catch (SegmentationFault e) {
                throw new IllegalStateException("Has the VM size changed?", e);
            }
        };

        ctx.getVm().set(supplier, addr);
        ctx.log("Created Function for '" + args[1] + "' at " + addr);
    }

}
