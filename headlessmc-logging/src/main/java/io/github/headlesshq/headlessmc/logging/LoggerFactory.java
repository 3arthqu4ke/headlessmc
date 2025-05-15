package io.github.headlesshq.headlessmc.logging;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
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
    @Setter
    @Getter
    private static Function<String, Logger> provider = str -> new Logger(java.util.logging.Logger.getLogger(str));

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }

    public static Logger getLogger(String name) {
        Logger logger = provider.apply(name);
        logger.setLevel(Level.ALL); // level is handled via handler
        return logger;
    }

}
