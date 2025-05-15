package io.github.headlesshq.headlessmc.runtime.commands.reflection;

import lombok.CustomLog;
import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.api.command.ParseUtil;
import io.github.headlesshq.headlessmc.runtime.reflection.RuntimeReflection;

@CustomLog
public class IfCommand extends AbstractRuntimeCommand {
    public IfCommand(RuntimeReflection ctx) {
        super(ctx, "if", "Only executes something if a value is true.");
        args.put("<addr>", "Address which might contain a Boolean.TRUE.");
        args.put("<if>", "Command to execute if the address is true.");
        args.put("<else>", "Command to execute if the address isn't true.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length < 2) {
            ctx.log("Please specify an address and a command to run!");
        } else if (args.length < 3) {
            ctx.log("Please specify a command to run!");
        } else {
            int address = ParseUtil.parseI(args[1]);
            Object obj = ctx.getVm().get(address);
            if (obj instanceof Boolean && (Boolean) obj) {
                log.debug("Executing if: " + args[2]);
                ctx.getCommandLine().getCommandContext().execute(args[2]);
            } else if (args.length > 3) {
                log.debug("Executing else: " + args[3]);
                ctx.getCommandLine().getCommandContext().execute(args[3]);
            }
        }
    }

}
