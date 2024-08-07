package me.earth.headlessmc.runtime;

import me.earth.headlessmc.api.command.line.CommandLineManager;
import me.earth.headlessmc.api.config.ConfigImpl;

public interface RuntimeTest {
    default Runtime getRuntime() {
        return RuntimeApi.init(ConfigImpl.empty(), new CommandLineManager());
    }

}
