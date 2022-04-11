package me.earth.headlessmc.api.command;

public interface CommandContext extends Iterable<Command> {
    void execute(String command);

}