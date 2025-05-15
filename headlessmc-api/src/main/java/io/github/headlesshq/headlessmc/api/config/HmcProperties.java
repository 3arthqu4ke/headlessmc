package io.github.headlesshq.headlessmc.api.config;

import io.github.headlesshq.headlessmc.logging.Logger;
import io.github.headlesshq.headlessmc.logging.LoggingProperties;

import static io.github.headlesshq.headlessmc.api.config.PropertyTypes.bool;
import static io.github.headlesshq.headlessmc.api.config.PropertyTypes.string;

/**
 * Properties shared by both the HeadlessMc-Runtime and the
 * HeadlessMc-Launcher.
 */
public interface HmcProperties {
    /**
     * The Minecraft main method passed to the runtime so it can call it.
     */
    Property<String> MAIN = string("hmc.main_method");

    /**
     * If runtime is started with Java 9+ and the Minecraft main class needs to
     * get deencapsulated first before it can get called.
     */
    Property<Boolean> DEENCAPSULATE = bool("hmc.deencapsulate");

    /**
     * The initial LogLevel for HeadlessMcs {@link Logger}s.
     */
    Property<String> LOGLEVEL = string("hmc.loglevel");

    /**
     * Quits on a failed command. For more strictness in CI/CD pipelines.
     */
    Property<Boolean> EXIT_ON_FAILED_COMMAND = bool("hmc.exit.on.failed.command");

    /**
     * @see LoggingProperties#FILE_HANDLER_ENABLED
     */
    Property<Boolean> FILE_HANDLER_ENABLED = bool(LoggingProperties.FILE_HANDLER_ENABLED);

}
