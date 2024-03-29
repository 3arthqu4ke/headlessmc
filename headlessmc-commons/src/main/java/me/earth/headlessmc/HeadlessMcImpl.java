package me.earth.headlessmc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.LogsMessages;
import me.earth.headlessmc.api.PasswordAware;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.config.HasConfig;

@RequiredArgsConstructor
public class HeadlessMcImpl implements HeadlessMc {
    @Delegate
    private final LogsMessages log;
    @Delegate
    private final HasConfig configHolder;
    @Delegate
    private final PasswordAware passwordAware;
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
