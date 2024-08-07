package me.earth.headlessmc.launcher.plugin;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.HasDescription;
import me.earth.headlessmc.launcher.Launcher;
import org.jetbrains.annotations.NotNull;

public interface HeadlessMcPlugin extends HasName, HasDescription, Comparable<HeadlessMcPlugin> {
    int getPriority();

    void init(Launcher launcher);

    @Override
    default int compareTo(@NotNull HeadlessMcPlugin o) {
        int result = Integer.compare(this.getPriority(), o.getPriority());
        return result == 0 ? this.getName().compareTo(o.getName()) : result;
    }

}
