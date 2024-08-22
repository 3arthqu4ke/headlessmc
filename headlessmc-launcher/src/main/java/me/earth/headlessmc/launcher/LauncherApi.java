package me.earth.headlessmc.launcher;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * Holds a globally available instance of the HeadlessMc Launcher.
 */
@UtilityClass
public class LauncherApi {
    /**
     * A global instance of the HeadlessMc Launcher.
     */
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private static Launcher launcher;

}
