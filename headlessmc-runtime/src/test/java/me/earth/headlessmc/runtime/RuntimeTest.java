package me.earth.headlessmc.runtime;

import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.ConfigImpl;

public interface RuntimeTest {
    default Runtime getRuntime() {
        System.setProperty(RuntimeProperties.ENABLE_REFLECTION.getName(), "true");
        return RuntimeApi.init(ConfigImpl.empty(), new CommandLine());
    }

}
