package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.download.VersionArgument;
import me.earth.headlessmc.launcher.modlauncher.Modlauncher;
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.launcher.version.family.FamilyUtil;
import me.earth.headlessmc.launcher.version.family.HasParent;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractVersionCommand extends AbstractLauncherCommand
    implements FindByCommand<Version> {
    public AbstractVersionCommand(Launcher ctx, String name, String desc) {
        super(ctx, name, desc);
    }

    @Override
    public @Nullable Version findObject(boolean byId, boolean byRegex, String versionArg, String... args) throws CommandException {
        Version result = FindByCommand.super.findObject(byId, byRegex, versionArg, args);
        if (result == null) {
            VersionArgument versionArgument = VersionArgument.parseVersion(versionArg);
            return find(versionArgument, getIterable());
        }

        return result;
    }

    @Override
    public Iterable<Version> getIterable() {
        return ctx.getVersionService();
    }

    // completions for new version format?

    // for testing
    protected <V extends HasName & HasParent<V>> @Nullable V find(VersionArgument versionArgument, Iterable<V> versions) {
        for (V version : versions) {
            if (Modlauncher.getFromVersionName(version.getName()) == versionArgument.getModlauncher()
                    && (version.getName().equalsIgnoreCase(versionArgument.getName())
                    || FamilyUtil.anyMemberMatches(version, parent -> parent.getName().equalsIgnoreCase(versionArgument.getName())))) {
                return version;
            }
        }

        return null;
    }

}
