package me.earth.headlessmc.api;

import me.earth.headlessmc.api.command.line.CommandLineManager;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.logging.LoggingService;

public interface HeadlessMc extends LogsMessages, HasConfig {
    CommandLineManager getCommandLineManager();

    ExitManager getExitManager();

    LoggingService getLoggingService();

    @Override
    default void log(String message) {
        getCommandLineManager().getInAndOutProvider().getOut().get().println(message);
    }

}
