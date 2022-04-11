package me.earth.headlessmc.logging;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class Logger {
    @Delegate
    private final java.util.logging.Logger logger;

    public void debug(String message) {
        logger.fine(message);
    }

    public void error(String message) {
        logger.severe(message);
    }

}
