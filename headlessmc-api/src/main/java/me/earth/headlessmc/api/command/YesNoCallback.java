package me.earth.headlessmc.api.command;

public interface YesNoCallback {
    void accept(boolean result) throws CommandException;

}
