package me.earth.headlessmc.runtime.commands;

import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;
import me.earth.headlessmc.runtime.util.ClassHelper;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CustomLog
public class NewCommand extends AbstractReflectionCommand {
    public NewCommand(Runtime ctx) {
        super(ctx, "new", "Creates a new object.");
        args.put("<target>", "Address to store the created object at.");
        args.put("<constructor>",
                 "Optional, Signature of a specific constructor.");
        args.put("<args>", "Multiple addresses containing the arguments.");
    }

    @Override
    protected void execute(Object o, int address, String... args)
        throws CommandException {
        if (args.length < 3) {
            throw new CommandException("Please specify an address to store the"
                                           + " result of the <init> call to.");
        }

        Class<?> clazz = o instanceof Class ? (Class<?>) o : o.getClass();
        val helper = ClassHelper.of(clazz);
        List<Constructor<?>> ctrs = helper
            .getConstructors()
            .stream()
            .filter(c -> args.length > 3
                && ClassHelper.getArgs(true, c.getParameterTypes())
                              .equalsIgnoreCase(args[3]))
            .findFirst()
            // java generic insanity
            .<List<Constructor<?>>>map(Collections::singletonList)
            .orElseGet(() -> helper
                .getConstructors()
                .stream()
                .filter(c -> c.getParameterTypes().length == args.length - 3)
                .collect(Collectors.toList()));

        if (ctrs.size() == 0) {
            throw new CommandException("Couldn't find a constructor with <="
                                           + (args.length - 3)
                                           + " arg(s) in class "
                                           + clazz.getName()
                                           + "!");
        }

        val ctr = ctrs.get(0);
        if (ctrs.size() > 1) {
            ctx.log("Available Constructors:");
            ctx.log(ClassHelper.getConstructorTable(ctrs, true).build());
            ctx.log("There's multiple constructors matching with <="
                        + (args.length - 3)
                        + " args, please specify one like this: 'new "
                        + args[1] + " " + args[2] + " \""
                        + args[3] + "\" "
                        + ClassHelper.getArgs(true, ctr.getParameterTypes())
                        + " <parameter addresses>'");
            return;
        }

        if (args.length < ctr.getParameterTypes().length + 3) {
            throw new CommandException(
                "Please specify " + ctr.getParameterTypes().length
                    + " addresses to load parameters from and one to"
                    + " store the result into.");
        }

        Object[] arguments = parse(ctr.getParameterTypes(), args);
        int target = ParseUtil.parseI(args[2]);
        try {
            ctr.setAccessible(true);
            Object value = ctr.newInstance(arguments);
            ctx.getVm().set(value, target);
        } catch (Exception e) {
            log.error(e);
        }
    }

}
