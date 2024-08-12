package me.earth.headlessmc.runtime.commands.reflection;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.api.command.ParseUtil;
import me.earth.headlessmc.runtime.reflection.ClassHelper;
import me.earth.headlessmc.runtime.reflection.ClassUtil;
import me.earth.headlessmc.runtime.reflection.RuntimeReflection;

public class ClassCommand extends AbstractRuntimeCommand {
    public ClassCommand(RuntimeReflection ctx) {
        super(ctx, "class", "Get and load classes.");
        args.put("<name>", "Name of the class.");
        args.put("<addr>", "Address to store the class in.");
        args.put("-syscl", "If the SystemClassloader should be used.");
        args.put("-dump", "If information about the class should be printed.");
        args.put("-primitive", "If the given class if primitive.");
        args.put("-v", "If the class should get dumped verbosely.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
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
            Class<?> c = CommandUtil.hasFlag("-primitive", args)
                ? ClassUtil.getPrimitiveClass(args[1])
                : Class.forName(args[1], init, classLoader);

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
