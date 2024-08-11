package me.earth.headlessmc.api;

import me.earth.headlessmc.api.classloading.Deencapsulator;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.logging.LoggingService;

public interface HeadlessMc extends LogsMessages, HasConfig {
    Deencapsulator getDeencapsulator();

    CommandLine getCommandLine();

    ExitManager getExitManager();

    LoggingService getLoggingService();

    @Override
    default void log(String message) {
        getCommandLine().getInAndOutProvider().getOut().get().println(message);
    }

}
