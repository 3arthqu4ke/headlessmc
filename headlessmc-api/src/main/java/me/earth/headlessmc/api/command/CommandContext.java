package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.classloading.ApiClassloadingHelper;
import me.earth.headlessmc.api.command.line.CommandLine;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A {@link CommandContext}, it manages multiple {@link Command}s and is the bridge between them and a {@link CommandLine}.
 * Due to classloading problems, Commands and CommandContext both only expose methods
 * where parameters and return values are from the java.lang.package (see {@link ApiClassloadingHelper}).
 * @see CommandContextImpl
 * @see YesNoContext
 */
public interface CommandContext extends Iterable<Command> {
    /**
     * Executes the given command.
     * In the default implementation this means that a matching {@link Command} will be found and executed.
     *
     * @param command the string to execute.
     */
    void execute(String command);

    /**
     * Returns a list of possible completions for a string read from a CommandLine.
     * We use a {@link Map.Entry}, where the keys represent the completion and the values a description to display.
     * (The reason we do not expose a data class instead of Map.Entries is
     * because we can only share java classes easily between classloaders, see {@link ApiClassloadingHelper}).
     *
     * @param line the line from the CommandLine to complete.
     * @return a list of completions, where the keys are the completion and values a description to display.
     */
    default List<Map.Entry<String, @Nullable String>> getCompletions(String line) {
        String parsedLine = line.toLowerCase(Locale.ENGLISH);
        String[] args = CommandUtil.split(parsedLine);
        List<Map.Entry<String, @Nullable String>> result = new ArrayList<>();
        for (Command command : this) {
            if (args.length == 1 || command.matches(line, args)) {
                command.getCompletions(line, result, args);
            } else if (args.length == 0) {
                result.add(new AbstractMap.SimpleEntry<>(command.getName(), command.getDescription()));
            }
        }

        return result;
    }

}