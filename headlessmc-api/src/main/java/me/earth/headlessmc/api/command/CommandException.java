package me.earth.headlessmc.api.command;

import lombok.experimental.StandardException;

/**
 * A CommandException is meant to be used as a quick and easy way to exit the
 * execution of a command. It should only be used to communicate to the user
 * that something with his input went wrong.
 */
@StandardException
public class CommandException extends Exception {
    public CommandException(String message) {
        super(message);
    }

}
