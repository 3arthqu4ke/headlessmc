package io.github.headlesshq.headlessmc.runtime.commands.reflection;

import io.github.headlesshq.headlessmc.api.command.CommandException;

@FunctionalInterface
public interface TypeParser {
    Object parse(String in) throws CommandException;

}
