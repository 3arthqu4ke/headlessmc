package me.earth.headlessmc.api;

import me.earth.headlessmc.api.command.HasCommandContext;
import me.earth.headlessmc.api.process.InAndOutProvider;

/**
 * HeadlessMc can be started in two modes. Either we permanently listen for
 * commands and handle them until a quit command is used, or we send and execute
 * one command at startup (QuickExitCli). Since that command might need a
 * callback, e.g. answering Yes/No question, we can't exit  HeadlessMc right
 * away, that's what this class is for.
 */
// TODO: it's quickly getting hard to oversee where we are waiting for input!
public interface QuickExitCli extends HasCommandContext {
    InAndOutProvider getInAndOutProvider();

    boolean isQuickExitCli();

    void setQuickExitCli(boolean quickExitCli);

    boolean isWaitingForInput();

    void setWaitingForInput(boolean waitingForInput);

}