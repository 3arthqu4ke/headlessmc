package me.earth.headlessmc.launcher.specifics;

import me.earth.headlessmc.launcher.util.URLs;

/**
 * Common version specific mods we use.
 *
 * @see <a href=https://github.com/3arthqu4ke/hmc-specifics>https://github.com/3arthqu4ke/hmc-specifics</a>
 * @see <a href=https://github.com/3arthqu4ke/mc-runtime-test>https://github.com/3arthqu4ke/mc-runtime-test</a>
 * @see <a href=https://github.com/3arthqu4ke/hmc-optimizations>https://github.com/3arthqu4ke/hmc-optimizations</a>
 */
public class VersionSpecificMods {
    /**
     * Version specific implementations of the HeadlessMc runtime.
     * @see <a href=https://github.com/3arthqu4ke/hmc-specifics>https://github.com/3arthqu4ke/hmc-specifics</a>
     */
    public static final VersionSpecificModRepository HMC_SPECIFICS =
        new VersionSpecificModRepository(URLs.url("https://github.com/3arthqu4ke/hmc-specifics/releases/download/"), "hmc-specifics", "2.0.0", "-release");

    /**
     * Mods that join a SinglePlayer world, run GameTests and exit the game, for testing purposes.
     * @see <a href=https://github.com/3arthqu4ke/mc-runtime-test>https://github.com/3arthqu4ke/mc-runtime-test</a>
     */
    public static final VersionSpecificModRepository MC_RUNTIME_TEST =
        new VersionSpecificModRepository(URLs.url("https://github.com/3arthqu4ke/mc-runtime-test/releases/download/"), "mc-runtime-test", "2.2.0", "-release");

    // Not yet... public static final VersionSpecificModRepository HMC_OPTIMIZATIONS =
    //    new VersionSpecificModRepository(URLs.url("https://github.com/3arthqu4ke/hmc-optimizations/releases/download/"), "hmc-optimizations", "1.0.0", "-release");

}
