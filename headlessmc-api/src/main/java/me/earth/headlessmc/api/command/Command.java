package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.HasName;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface Command extends HasName, HasDescription, HasArguments {
    void execute(String... args) throws CommandException;

    boolean matches(String... args);

    default void getCompletions(String line, List<Map.Entry<String, @Nullable String>> completions, String... args) {
        if (getName().toLowerCase(Locale.ENGLISH).startsWith(line)) {
            completions.add(new AbstractMap.SimpleEntry<>(getName(), getDescription()));
        }
    }

}
