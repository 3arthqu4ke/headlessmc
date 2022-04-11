package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.ParseUtil;
import me.earth.headlessmc.runtime.Runtime;
import me.earth.headlessmc.runtime.util.ClassHelper;

import java.lang.reflect.Field;

public class FieldCommand extends AbstractReflectionCommand {
    public FieldCommand(Runtime ctx) {
        super(ctx, "field", "Gets a field.");
    }

    @Override
    protected void execute(Object obj, int address, String... args)
        throws CommandException {
        Class<?> clazz = obj instanceof Class ? (Class<?>) obj : obj.getClass();
        ClassHelper helper = ClassHelper.of(clazz);
        Field field = helper
            .getFields()
            .stream()
            .filter(f -> f.getName().equals(args[2]))
            .findFirst()
            .orElseThrow(() -> new CommandException(
                "Couldn't find Field with name " + args[2]));

        if (args.length < 4) {
            throw new CommandException(
                "Please specify an address to store the field in.");
        }

        int target = ParseUtil.parseI(args[3]);
        try {
            field.setAccessible(true);
            Object value = field.get(obj);
            ctx.getVm().set(value, target);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
