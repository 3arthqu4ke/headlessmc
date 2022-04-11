package me.earth.headlessmc.api.config;

/**
 * A type which holds a {@link Config}.
 */
@FunctionalInterface
public interface HasConfig {
    /**
     * @return the config held by this object.
     */
    Config getConfig();

}
