package me.earth.headlessmc.runtime.commands;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;

@CustomLog
public class IfCommand extends AbstractRuntimeCommand {
    public IfCommand(Runtime ctx) {
        super(ctx, "if", "Only executes something if a value is true.");
        args.put("<addr>", "Address which might contain a Boolean.TRUE.");
        args.put("<if>", "Command to execute if the address is true.");
        args.put("<else>", "Command to execute if the address isn't true.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 2) {
            ctx.log("Please specify an address and a command to run!");
        } else if (args.length < 3) {
            ctx.log("Please specify a command to run!");
        } else {
            int address = ParseUtil.parseI(args[1]);
            Object obj = ctx.getVm().get(address);
            if (obj instanceof Boolean && (Boolean) obj) {
                log.debug("Executing if: " + args[2]);
                ctx.getCommandContext().execute(args[2]);
            } else if (args.length > 3) {
                log.debug("Executing else: " + args[3]);
                ctx.getCommandContext().execute(args[3]);
            }
        }
    }

}
