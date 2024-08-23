package me.earth.headlessmc.api;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.classloading.Deencapsulator;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.logging.LoggingService;

/**
 * A default implementation for the {@link HeadlessMc} interface.
 */
@Getter
@RequiredArgsConstructor
public class HeadlessMcImpl implements HeadlessMc {
    private final Deencapsulator deencapsulator = new Deencapsulator();
    @Delegate
    @Getter(AccessLevel.NONE)
    private final HasConfig configHolder;
    private final CommandLine commandLine;
    private final ExitManager exitManager;
    private final LoggingService loggingService;

}
