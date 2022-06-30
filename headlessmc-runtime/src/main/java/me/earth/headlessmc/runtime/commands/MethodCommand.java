package me.earth.headlessmc.runtime.commands;

import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;
import me.earth.headlessmc.runtime.util.ClassHelper;

import java.util.stream.Collectors;

public class MethodCommand extends AbstractReflectionCommand {
    public MethodCommand(Runtime ctx) {
        super(ctx, "method", "Invokes a method.");
    }

    @Override
    protected void execute(Object o, int address, String... args)
        throws CommandException {
        if (args.length < 4) {
            throw new CommandException("Please specify an address to store the"
                                           + " result of the method call to.");
        }

        Class<?> clazz = o instanceof Class ? (Class<?>) o : o.getClass();
        val helper = ClassHelper.of(clazz);
        // TODO: this is fucked rn
        val methods = helper
            .getMethods()
            .stream()
            .filter(m -> m.getName().equals(args[2])
                || args.length > 4
                && ClassHelper.getArgs(true, m.getParameterTypes())
                              .equals(args[4]))
            .collect(Collectors.toList());

        if (methods.size() == 0) {
            throw new CommandException("Couldn't find Method " + args[2]
                                           + " in class " + clazz.getName()
                                           + "!");
        }

        val method = methods.get(0);
        if (methods.size() > 1) {
            ctx.log(ClassHelper.getMethodTable(methods, true).build());
            ctx.log("There's multiple methods matching " + args[2]
                        + " please specify one like this: ["
                        + method.getName() + " "
                        + args[3] + " \""
                        + ClassHelper.getArgs(true, method.getParameterTypes())
                        + "\"]");
            return;
        }

        if (args.length < method.getParameterTypes().length + 4) {
            throw new CommandException(
                "Please specify " + method.getParameterTypes().length
                    + " addresses to load parameters from and one to"
                    + " store the result into.");
        }

        Object[] arguments = parse(method.getParameterTypes(), args);
        int target = ParseUtil.parseI(args[3]);
        try {
            method.setAccessible(true);
            Object value = method.invoke(o, arguments);
            ctx.getVm().set(value, target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
