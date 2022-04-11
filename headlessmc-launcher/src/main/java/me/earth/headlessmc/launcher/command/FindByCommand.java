package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandUtil;

public interface FindByCommand<T extends HasName & HasId> extends Command {
    void execute(T obj, String... args) throws CommandException;

    Iterable<T> getIterable();

    @Override
    default void execute(String... args) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("Please specify a Version!");
        }

        boolean byId = CommandUtil.hasFlag("-id", args);
        T t = byId
            ? HasId.getById(args[1], getIterable())
            : HasName.getByName(args[1], getIterable());

        if (t == null) {
            throw new CommandException("Couldn't find object for "
                                           + (byId ? "id '" : "name '")
                                           + args[1] + "'!");
        }

        this.execute(t, args);
    }

}
