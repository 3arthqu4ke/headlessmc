package me.earth.headlessmc.config;

import me.earth.headlessmc.api.config.Property;
import me.earth.headlessmc.logging.Logger;

import static me.earth.headlessmc.config.PropertyTypes.bool;
import static me.earth.headlessmc.config.PropertyTypes.string;

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
     * If runtime is started with Java > 8 and the Minecraft main class needs to
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

}
