package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.classloading.ApiClassloadingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Represents an executable command in HeadlessMc.
 * Commands are managed by a {@link CommandContext}.
 * Due to classloading problems, Commands and CommandContext both only expose methods
 * where parameters and return values are from the java packages (see {@link ApiClassloadingHelper}).
 */
public interface Command extends HasName, HasDescription, HasArguments {
    /**
     * Executes this command.
     *
     * @param line the raw line read from the command line.
     * @param args the arguments parsed by {@link CommandUtil#split(String)}.
     * @throws CommandException if something went wrong while executing this command.
     */
    void execute(String line, String... args) throws CommandException;

    /**
     * Used by a {@link CommandContext} to decide whether to execute this command or not.
     *
     * @param line the raw line read from the command line.
     * @param args the arguments parsed by {@link CommandUtil#split(String)}.
     * @return {@code true} if this command matches the given arguments.
     */
    boolean matches(String line, String... args);

    /**
     * Adds possible completions for the CommandLine to the given completion list.
     * We use a {@link Map.Entry}, where the keys represent the completion and the values a description to display.
     * (The reason we do not expose a data class instead of Map.Entries is
     * because we can only share java classes easily between classloaders, see {@link ApiClassloadingHelper}).
     *
     * @param line the line from the CommandLine to complete.
     * @param completions a mutable list of completions this method should add to.
     * @param args the line, just split by {@link CommandUtil#split(String)}.
     */
    default void getCompletions(String line, List<Map.Entry<String, @Nullable String>> completions, String... args) {
        if (getName().toLowerCase(Locale.ENGLISH).startsWith(line)) {
            completions.add(new AbstractMap.SimpleEntry<>(getName(), getDescription()));
        }
    }

}
