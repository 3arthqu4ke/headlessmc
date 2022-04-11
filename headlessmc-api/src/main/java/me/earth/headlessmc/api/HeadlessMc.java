package me.earth.headlessmc.api;

import me.earth.headlessmc.api.command.HasCommandContext;
import me.earth.headlessmc.api.config.HasConfig;

public interface HeadlessMc
    extends HasCommandContext, LogsMessages, HasConfig, PasswordAware {

}
