package me.earth.headlessmc.launcher;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LauncherApi {
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private static Launcher launcher;

}
