package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.HasName;

public interface Command extends HasName, HasDescription, HasArguments {
    void execute(String... args) throws CommandException;

    boolean matches(String... args);

}
