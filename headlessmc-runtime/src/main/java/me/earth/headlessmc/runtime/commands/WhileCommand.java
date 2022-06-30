package me.earth.headlessmc.runtime.commands;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;

@CustomLog
public class WhileCommand extends AbstractRuntimeCommand {
    public WhileCommand(Runtime ctx) {
        super(ctx, "while", "Repeatedly executes a command.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 2) {
            ctx.log("Please specify an address and a command to run!");
        } else if (args.length < 3) {
            ctx.log("Please specify a command to run!");
        } else {
            int address = ParseUtil.parseI(args[1]);
            ctx.getVm().checkSegfault(address);

            int maxLoops = -1;
            if (args.length > 3) {
                maxLoops = ParseUtil.parseI(args[3]);
            }

            int loop = 0;
            while (maxLoops == -1 || loop++ < maxLoops) {
                log.debug("Iteration: " + loop);
                Object obj = ctx.getVm().get(address);
                if (obj instanceof Boolean && (Boolean) obj) {
                    log.debug("Executing " + args[2]);
                    ctx.getCommandContext().execute(args[2]);
                } else {
                    break;
                }
            }

            log.debug("While-Loop ended.");
        }
    }

}
