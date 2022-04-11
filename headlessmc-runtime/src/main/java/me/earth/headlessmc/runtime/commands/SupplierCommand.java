package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;
import me.earth.headlessmc.runtime.SegmentationFault;

import java.util.function.Supplier;

public class SupplierCommand extends AbstractRuntimeCommand {
    public SupplierCommand(Runtime ctx) {
        super(ctx, "supplier", "Turns the given command into a supplier.");
    }

    @Override
    public void execute(String... args) throws CommandException {
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
                    ctx.getCommandContext().execute(args[1]);
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
