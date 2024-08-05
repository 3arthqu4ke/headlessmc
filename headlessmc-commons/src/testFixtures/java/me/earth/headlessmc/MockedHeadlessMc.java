package me.earth.headlessmc;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.command.CommandContextImpl;
import me.earth.headlessmc.config.ConfigImpl;

@Getter
@Setter
public class MockedHeadlessMc implements HeadlessMc {
    // no enum cause @Setter
    public static final MockedHeadlessMc INSTANCE = new MockedHeadlessMc();
    private CommandContext commandContext = new CommandContextImpl(this);
    private MockedExitManager exitManager = new MockedExitManager();
    private Config config = ConfigImpl.empty();
    private boolean hidingPasswords = false;
    private boolean waitingForInput = false;
    private boolean quickExitCli = false;
    private boolean hidingPasswordsSupported = true;
    private String log = null;

    @Override
    public void log(String message) {
        this.log = message;
    }

    public static class MockedExitManager extends ExitManager {
        public MockedExitManager() {
            setExitManager(i -> {});
        }
    }

}
