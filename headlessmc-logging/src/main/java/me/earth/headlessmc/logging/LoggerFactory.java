package me.earth.headlessmc.logging;

import lombok.experimental.UtilityClass;

import java.util.logging.Level;

/**
 * Custom LoggerFactory for {@link lombok.CustomLog}. Created because {@link
 * Class#getSimpleName()} is cooler.
 *
 * TODO: is it really though?
 */
@UtilityClass
@SuppressWarnings("unused") // see lombok.config
public class LoggerFactory {
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }

    public static Logger getLogger(String name) {
        Logger logger = new Logger(java.util.logging.Logger.getLogger(name));
        logger.setLevel(Level.ALL); // level is handled via handler
        return logger;
    }

}
