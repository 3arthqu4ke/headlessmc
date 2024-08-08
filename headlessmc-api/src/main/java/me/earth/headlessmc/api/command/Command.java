package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.HasName;

import java.util.List;
import java.util.Locale;

public interface Command extends HasName, HasDescription, HasArguments {
    void execute(String... args) throws CommandException;

    boolean matches(String... args);

    default void getCompletions(String line, List<Completion> completions, String... args) {
        if (getName().toLowerCase(Locale.ENGLISH).startsWith(line)) {
            completions.add(new Completion(getName(), getDescription()));
        }
    }

}
