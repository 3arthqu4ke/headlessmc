package me.earth.headlessmc.api;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.logging.LoggingService;

@Getter
@Setter
public class MockedHeadlessMc implements HeadlessMc {
    // no enum cause @Setter
    public static final MockedHeadlessMc INSTANCE = new MockedHeadlessMc();
    private MockedExitManager exitManager = new MockedExitManager();
    private CommandLine commandLine = new CommandLine();
    private LoggingService loggingService = new LoggingService();
    private Config config = ConfigImpl.empty();
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
