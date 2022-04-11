package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;
import me.earth.headlessmc.runtime.util.ClassHelper;

public class ClassCommand extends AbstractRuntimeCommand {
    public ClassCommand(Runtime ctx) {
        super(ctx, "class", "Get and load classes.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException(
                "Please specify a class and an address!");
        }

        ClassLoader classLoader = ctx.getMainThread().getContextClassLoader();
        if (CommandUtil.hasFlag("-syscl", args)) {
            classLoader = ClassLoader.getSystemClassLoader();
        }

        boolean init = CommandUtil.hasFlag("-init", args);
        try {
            Class<?> c = Class.forName(args[1], init, classLoader);
            if (CommandUtil.hasFlag("-dump", args)) {
                ClassHelper.of(c).dump(ctx, CommandUtil.hasFlag("-v", args));
            }

            int address = ParseUtil.parseI(args[2]);
            ctx.getVm().set(c, address);
        } catch (ClassNotFoundException e) {
            throw new CommandException("Couldn't find class: " + args[1]
                                           + " on ClassLoader "
                                           + classLoader.getClass().getName()
                                           + ": " + e.getMessage());
        }
    }

}
