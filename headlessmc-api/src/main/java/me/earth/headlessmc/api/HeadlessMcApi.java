package me.earth.headlessmc.api;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.classloading.ApiClassloadingHelper;
import me.earth.headlessmc.api.classloading.ClAgnosticCommandContext;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.command.line.CommandLineReader;

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
     * If this API supports {@link ClAgnosticCommandContext}s.
     * @see ApiClassloadingHelper
     */
    @Getter
    @Setter
    private static boolean supportingClassloadingAgnosticContexts = true;
    /**
     * A global instance of {@link HeadlessMc}.
     */
    @Getter
    @Setter
    private static HeadlessMc instance;

}
