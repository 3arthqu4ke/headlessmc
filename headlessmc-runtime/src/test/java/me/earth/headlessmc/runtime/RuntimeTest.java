package me.earth.headlessmc.runtime;

import me.earth.headlessmc.command.line.PasswordAwareImpl;
import me.earth.headlessmc.config.ConfigImpl;

public interface RuntimeTest {
    default Runtime getRuntime() {
        return RuntimeApi.init(ConfigImpl.empty(), new PasswordAwareImpl());
    }

}
