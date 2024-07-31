package me.earth.headlessmc.auth;

import net.raphimc.minecraftauth.util.logging.ILogger;

public enum NoLogging implements ILogger {
    INSTANCE;

    @Override
    public void info(String s) {
        // NOP
    }

    @Override
    public void warn(String s) {
        // NOP
    }

    @Override
    public void error(String s) {
        // NOP
    }

}
