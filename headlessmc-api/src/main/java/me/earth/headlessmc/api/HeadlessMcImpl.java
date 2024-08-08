package me.earth.headlessmc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.logging.LoggingService;

@RequiredArgsConstructor
public class HeadlessMcImpl implements HeadlessMc {
    @Delegate
    private final HasConfig configHolder;
    @Getter
    private final CommandLine commandLine;
    @Getter
    private final ExitManager exitManager;
    @Getter
    private final LoggingService loggingService;

}
