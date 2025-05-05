package me.earth.headlessmc.api;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.classloading.ApiClassloadingHelper;
import me.earth.headlessmc.api.classloading.ClAgnosticCommandContext;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.command.line.CommandLineReader;

import java.util.function.Consumer;

/**
 * Provides a global instance of {@link HeadlessMc}.
 * The main reason we provide this is, that we only want to open one {@link CommandLineReader} per JVM.
 * If a CommandLineReader is already open, it would be best to install any {@link CommandContext}
 * on the {@link CommandLine} responsible for it instead of opening an own CommandLineReader.
 * One case where multiple HMC instances exist would be when starting the game with both the Runtime and HMC-Specifics active.
 * To make things more complicated the Runtime instance might be loaded through the system classloader,
 * while the HMC-Specifics instance will be loaded through the modloaders classloader.
 * In the worst case multiple classes named HeadlessMcApi exist on multiple Classloaders.
 * For that the {@link ApiClassloadingHelper} exists.
 */
public class HeadlessMcApi {
    /**
     * The current version of HeadlessMc.
     */
    public static final String VERSION = "2.6.1";
    /**
     * The string "HeadlessMC".
     */
    public static final String NAME = "HeadlessMC";

    private static final HmcInstanceHolder INSTANCE_HOLDER = new HmcInstanceHolder();
    /**
     * If this API supports {@link ClAgnosticCommandContext}s.
     * @see ApiClassloadingHelper
     */
    @Getter
    @Setter
    private static volatile boolean supportingClassloadingAgnosticContexts = true;

    /**
     * @return a global instance of {@link HeadlessMc}.
     */
    public static HeadlessMc getInstance() {
        return INSTANCE_HOLDER.getInstance();
    }

    /**
     * Sets the global instance of {@link HeadlessMc}.
     *
     * @param instance the global instance.
     * @see #getInstance()
     */
    public static void setInstance(HeadlessMc instance) {
        INSTANCE_HOLDER.setInstance(instance);
    }

    /**
     * Adds a listener that will get called if {@link #setInstance(HeadlessMc)} is called.
     * If an instance is already present it will get notified immediately.
     * This may happen asynchronously.
     * It is also possible that the listener gets called multiple times if someone installs another or the same instance of HeadlessMc multiple times.
     *
     * @param listener the listener that listens for global instances of {@link HeadlessMc} being set here.
     */
    public static void addListener(Consumer<HeadlessMc> listener) {
        INSTANCE_HOLDER.addListener(listener);
    }

}
