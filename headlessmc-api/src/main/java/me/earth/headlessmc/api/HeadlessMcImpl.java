package me.earth.headlessmc.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.line.CommandLineManager;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.api.process.InAndOutProvider;
import me.earth.headlessmc.logging.LoggingService;

@RequiredArgsConstructor
public class HeadlessMcImpl implements HeadlessMc {
    @Delegate
    private final HasConfig configHolder;
    @Getter
    private final CommandLineManager commandLineManager;
    @Getter
    private final ExitManager exitManager;
    @Getter
    private final LoggingService loggingService;
    @Getter
    private final InAndOutProvider inAndOutProvider;
    @Getter
    @Setter
    private CommandContext commandContext;
    @Getter
    @Setter
    private boolean waitingForInput;
    @Getter
    @Setter
    private boolean quickExitCli;

}
