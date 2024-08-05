package me.earth.headlessmc.logging;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.util.logging.Level;

// TODO: move in separate module!
@RequiredArgsConstructor
public class Logger {
    @Delegate
    private final java.util.logging.Logger logger;

    public void debug(String message) {
        logger.fine(message);
    }

    public void debug(String message, Throwable throwable) {
        logger.log(Level.FINE, message, throwable);
    }

    public void info(Throwable throwable) {
        logger.log(Level.INFO, "", throwable);
    }

    public void error(String message) {
        logger.severe(message);
    }

    public void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }

    public void error(Throwable throwable) {
        logger.log(Level.SEVERE, "", throwable);
    }

    public void warn(String message) {
        logger.warning(message);
    }

    public void warn(String message, Throwable throwable) {
        logger.log(Level.WARNING, message, throwable);
    }

    public void warn(Throwable throwable) {
        logger.log(Level.WARNING, "", throwable);
    }

}
