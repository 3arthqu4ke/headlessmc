package io.github.headlesshq.headlessmc.logging;

import io.github.headlesshq.headlessmc.logging.handlers.HmcFileHandler;
import io.github.headlesshq.headlessmc.logging.handlers.HmcStreamHandler;

/**
 * System Properties to configure logging.
 */
public interface LoggingProperties {
    /**
     * The initial log level for the {@link HmcStreamHandler}.
     */
    String LOG_LEVEL = "hmc.loglevel";
    /**
     * The initial log level for the {@link HmcFileHandler}.
     */
    String FILE_LOG_LEVEL = "hmc.fileloglevel";
    /**
     * If the {@link HmcFileHandler} is enabled.
     */
    String FILE_HANDLER_ENABLED = "hmc.filehandler.enabled";

}
