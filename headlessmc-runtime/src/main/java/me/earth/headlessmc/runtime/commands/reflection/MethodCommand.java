package me.earth.headlessmc.runtime.commands.reflection;

import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.ParseUtil;
import me.earth.headlessmc.runtime.reflection.ClassHelper;
import me.earth.headlessmc.runtime.reflection.RuntimeReflection;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@CustomLog
public class MethodCommand extends AbstractVMCommand {
    public MethodCommand(RuntimeReflection ctx) {
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
        val method = getMethod(clazz, args);

        if (args.length < method.getParameterTypes().length + 4) {
            throw new CommandException(
                "Please specify " + method.getParameterTypes().length
                    + " addresses to load parameters from and one to"
                    + " store the result into.");
        }

        Object[] arguments = parse(method.getParameterTypes(), args);
        int target = ParseUtil.parseI(
            args[args.length - method.getParameterTypes().length - 1]);
        try {
            method.setAccessible(true);
            Object value = method.invoke(o, arguments);
            ctx.getVm().set(value, target);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private Method getMethod(Class<?> clazz, String... args)
        throws CommandException {
        val helper = ClassHelper.of(clazz);
        List<Method> methods = helper
            .getMethods()
            .stream()
            .filter(m -> m.getName().equals(args[2]))
            .collect(Collectors.toList());

        if (methods.isEmpty()) {
            throw new CommandException(
                "Couldn't find a method for name '" + args[2] + "' in class "
                    + helper.getClazz().getName());
        }

        Method result = methods.get(0);
        if (methods.size() > 1) {
            val filteredByArgs = methods
                .stream()
                .filter(m -> ClassHelper.getArgs(true, m.getParameterTypes())
                                        .equals(args[3]))
                .findFirst();

            if (!filteredByArgs.isPresent()) {
                ctx.log("Following methods with name '" + args[2] + "' are "
                            + "available in class " + clazz.getName() + ":");
                ctx.log(ClassHelper.getMethodTable(methods, true).build());
                throw new CommandException(
                    "Couldn't find method with arguments " + args[3]
                        + ", please specify arg types, e.g. '" + args[0]
                        + " " + args[1] + " " + args[2] + " \""
                        + ClassHelper.getArgs(true, result.getParameterTypes())
                        + "\"" + joinArray(3, args) + "'.");
            }

            result = filteredByArgs.get();
        }

        return result;
    }

    @SuppressWarnings("SameParameterValue")
    private String joinArray(int startIndex, String... args) {
        val sb = new StringBuilder();
        for (int i = startIndex; i < args.length; i++) {
            sb.append(" ").append(args[i]);
        }

        return sb.toString();
    }

}
