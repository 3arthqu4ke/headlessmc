package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.version.Version;

public abstract class AbstractVersionCommand extends AbstractLauncherCommand
    implements FindByCommand<Version> {
    public AbstractVersionCommand(Launcher ctx, String name, String desc) {
        super(ctx, name, desc);
    }

    @Override
    public Iterable<Version> getIterable() {
        return ctx.getVersionService();
    }

}
