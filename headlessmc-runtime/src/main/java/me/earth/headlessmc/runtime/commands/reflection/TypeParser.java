package me.earth.headlessmc.runtime.commands.reflection;

import me.earth.headlessmc.api.command.CommandException;

@FunctionalInterface
public interface TypeParser {
    Object parse(String in) throws CommandException;

}
