package io.github.headlesshq.headlessmc.api.command;

public interface YesNoCallback {
    void accept(boolean result) throws CommandException;

}
