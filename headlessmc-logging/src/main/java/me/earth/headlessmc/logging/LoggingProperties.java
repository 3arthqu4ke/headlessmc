package me.earth.headlessmc.logging;

/**
 * System Properties to configure logging.
 */
public interface LoggingProperties {
    /**
     * The initial log level for the {@link me.earth.headlessmc.logging.handlers.HmcStreamHandler}.
     */
    String LOG_LEVEL = "hmc.loglevel";
    /**
     * The initial log level for the {@link me.earth.headlessmc.logging.handlers.HmcFileHandler}.
     */
    String FILE_LOG_LEVEL = "hmc.fileloglevel";
    /**
     * If the {@link me.earth.headlessmc.logging.handlers.HmcFileHandler} is enabled.
     */
    String FILE_HANDLER_ENABLED = "hmc.filehandler.enabled";

}
