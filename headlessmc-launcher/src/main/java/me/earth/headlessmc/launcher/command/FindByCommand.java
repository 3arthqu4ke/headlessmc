package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.api.command.HasDescription;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public interface FindByCommand<T extends HasName & HasId> extends Command {
    void execute(T obj, String... args) throws CommandException;

    Iterable<T> getIterable();

    @Override
    default void execute(String line, String... args) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("Please specify an id!");
        }

        boolean byId = CommandUtil.hasFlag("-id", args);
        boolean byRegex = CommandUtil.hasFlag("-regex", args);
        if (byId && byRegex) {
            throw new CommandException("Both -id and -regex specified!");
        }

        try {
            T t = findObject(byId, byRegex, args[1], args);
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

    default @Nullable T findObject(boolean byId, boolean byRegex, String versionArg, String... args) throws CommandException {
        return byId
                ? HasId.getById(versionArg, getIterable())
                : byRegex
                 ? HasName.getByRegex(Pattern.compile(versionArg), getIterable())
                 : HasName.getByName(versionArg, getIterable());
    }

    @Override
    default void getCompletions(String line, List<Map.Entry<String, @Nullable String>> completions, String... args) {
        Command.super.getCompletions(line, completions, args);
        if (args.length == 2) {
            String arg = args[1].toLowerCase(Locale.ENGLISH);
            for (T t : getIterable()) {
                if (t.getName().toLowerCase(Locale.ENGLISH).startsWith(arg)) {
                    completions.add(new AbstractMap.SimpleEntry<>(t.getName(), t instanceof HasDescription ? ((HasDescription) t).getDescription() : null));
                }
            }
        }
    }

}
