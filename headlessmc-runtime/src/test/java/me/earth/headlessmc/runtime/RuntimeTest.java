package me.earth.headlessmc.runtime;

import me.earth.headlessmc.command.line.PasswordAwareImpl;
import me.earth.headlessmc.config.ConfigImpl;

public class RuntimeTest {
    protected final Runtime ctx = RuntimeApi.init(ConfigImpl.empty(),
                                                  new PasswordAwareImpl());

}
