package me.earth.headlessmc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.PasswordAware;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.api.process.InAndOutProvider;
import me.earth.headlessmc.logging.LoggingService;

@RequiredArgsConstructor
public class HeadlessMcImpl implements HeadlessMc {
    @Delegate
    private final HasConfig configHolder;
    @Delegate
    private final PasswordAware passwordAware;
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
