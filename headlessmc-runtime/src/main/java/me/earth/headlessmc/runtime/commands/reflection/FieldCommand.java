package me.earth.headlessmc.runtime.commands.reflection;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.api.command.ParseUtil;
import me.earth.headlessmc.runtime.reflection.ClassHelper;
import me.earth.headlessmc.runtime.reflection.RuntimeReflection;

import java.lang.reflect.Field;

@CustomLog
public class FieldCommand extends AbstractVMCommand {
    public FieldCommand(RuntimeReflection ctx) {
        super(ctx, "field", "Gets/sets a field.");
        args.put("<name>", "Name of the field.");
        args.put("<get/set>",
                 "Address to store the field into, or to set the field from.");
        args.put("-set", "If the field should be set instead of retrieved.");
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
            if (CommandUtil.hasFlag("-set", args)) {
                field.set(obj, ctx.getVm().get(target));
            } else {
                Object value = field.get(obj);
                ctx.getVm().set(value, target);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

}
