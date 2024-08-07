package me.earth.headlessmc.api;

import me.earth.headlessmc.api.command.HasCommandContext;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.logging.LoggingService;

public interface HeadlessMc extends HasCommandContext, LogsMessages, HasConfig, PasswordAware, QuickExitCli {
    ExitManager getExitManager();

    LoggingService getLoggingService();

    @Override
    default void log(String message) {
        getInAndOutProvider().getOut().get().println(message);
    }

}
