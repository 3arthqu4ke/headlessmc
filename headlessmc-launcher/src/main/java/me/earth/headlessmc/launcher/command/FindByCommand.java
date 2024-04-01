package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandUtil;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public interface FindByCommand<T extends HasName & HasId> extends Command {
    void execute(T obj, String... args) throws CommandException;

    Iterable<T> getIterable();

    @Override
    default void execute(String... args) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("Please specify a Version!");
        }

        boolean byId = CommandUtil.hasFlag("-id", args);
        boolean byRegex = CommandUtil.hasFlag("-regex", args);
        if (byId && byRegex) {
            throw new CommandException("Both -id and -regex specified!");
        }

        try {
            T t = byId
                ? HasId.getById(args[1], getIterable())
                : byRegex
                    ? HasName.getByRegex(Pattern.compile(args[1]), getIterable())
                    : HasName.getByName(args[1], getIterable());

            if (t == null) {
                throw new CommandException("Couldn't find object for "
                                               + (byId ? "id '" : "name '")
                                               + args[1] + "'!");
            }

            this.execute(t, args);
        } catch (PatternSyntaxException e) {
            throw new CommandException("Failed to parse regex " + args[1], e);
        }
    }

}
