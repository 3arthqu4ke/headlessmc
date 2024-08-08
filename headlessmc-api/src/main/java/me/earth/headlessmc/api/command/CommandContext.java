package me.earth.headlessmc.api.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public interface CommandContext extends Iterable<Command> {
    void execute(String command);

    default List<Completion> getCompletions(String line) {
        String parsedLine = line.toLowerCase(Locale.ENGLISH);
        String[] args = CommandUtil.split(parsedLine);
        List<Completion> result = new ArrayList<>();
        for (Command command : this) {
            command.getCompletions(line, result, args);
        }

        return result;
    }

}