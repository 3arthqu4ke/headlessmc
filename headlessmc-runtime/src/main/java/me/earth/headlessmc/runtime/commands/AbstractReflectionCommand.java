package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;

public abstract class AbstractReflectionCommand extends AbstractRuntimeCommand {
    public AbstractReflectionCommand(Runtime ctx, String name, String desc) {
        super(ctx, name, desc);
    }

    protected abstract void execute(Object obj, int addr, String... args)
        throws CommandException;

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException("Specify an owner and name!");
        }

        int address = ParseUtil.parseI(args[1]);
        Object obj = ctx.getVm().get(address);
        if (obj == null) {
            throw new CommandException("There's no Object at " + address);
        }

        this.execute(obj, address, args);
    }

    protected Object[] parse(Class<?>[] parameterTypes, String... args)
        throws CommandException {
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < arguments.length; i++) {
            int argAddress = ParseUtil.parseI(args[args.length - i - 1]);
            arguments[arguments.length - i - 1] = ctx.getVm().get(argAddress);
        }

        return arguments;
    }

}
