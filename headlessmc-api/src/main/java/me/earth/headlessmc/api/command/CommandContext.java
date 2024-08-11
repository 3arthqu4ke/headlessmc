package me.earth.headlessmc.api.command;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public interface CommandContext extends Iterable<Command> {
    void execute(String command);

    default List<Map.Entry<String, @Nullable String>> getCompletions(String line) {
        String parsedLine = line.toLowerCase(Locale.ENGLISH);
        String[] args = CommandUtil.split(parsedLine);
        List<Map.Entry<String, @Nullable String>> result = new ArrayList<>();
        for (Command command : this) {
            if (args.length == 1 || command.matches(args)) {
                command.getCompletions(line, result, args);
            } else if (args.length == 0) {
                result.add(new AbstractMap.SimpleEntry<>(command.getName(), command.getDescription()));
            }
        }

        return result;
    }

}