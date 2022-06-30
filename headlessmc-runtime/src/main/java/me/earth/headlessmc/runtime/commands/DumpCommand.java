package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;
import me.earth.headlessmc.runtime.util.ClassHelper;

public class DumpCommand extends AbstractRuntimeCommand {
    public DumpCommand(Runtime ctx) {
        super(ctx, "dump", "Dumps the object at the given memory address.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("Please specify an address!");
        }

        if (args[1].equalsIgnoreCase("-vm")) {
            for (int i = 0; i < ctx.getVm().size(); i++) {
                Object obj = ctx.getVm().get(i);
                if (obj != null) {
                    ctx.log(i + " : " + obj);
                }
            }
        } else {
            int address = ParseUtil.parseI(args[1]);
            Object obj = ctx.getVm().get(address);
            if (obj instanceof Class) {
                Class<?> c = (Class<?>) obj;
                ClassHelper.of(c).dump(ctx, CommandUtil.hasFlag("-v", args));
            } else {
                ctx.log(obj == null ? "null" : obj.toString());
            }
        }
    }

}
