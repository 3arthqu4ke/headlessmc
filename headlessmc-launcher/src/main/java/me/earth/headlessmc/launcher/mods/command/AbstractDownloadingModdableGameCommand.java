package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.api.VersionId;
import me.earth.headlessmc.launcher.mods.ModdableGame;
import me.earth.headlessmc.launcher.mods.ModdableGameProvider;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDownloadingModdableGameCommand extends AbstractModdableGameCommand {
    public AbstractDownloadingModdableGameCommand(Launcher ctx, String name, String desc, ModdableGameProvider provider) {
        super(ctx, name, desc, provider);
    }

    @Override
    public @Nullable ModdableGame findObject(boolean byId, boolean byRegex, String versionArg, String... args) throws CommandException {
        ModdableGame result = super.findObject(byId, byRegex, versionArg, args);
        if (result == null
                && !byId
                && !byRegex
                && !CommandUtil.hasFlag("-norecursivedownload", args)
                && ctx.getConfig().get(LauncherProperties.AUTO_DOWNLOAD, true)) {
            VersionId versionArgument = VersionId.parse(versionArg);
            // this is a bit frail, it's basically the AbstractVersionDownloadingCommand...
            provider.download(ctx, versionArgument, args);
            result = super.findObject(false, false, versionArg, args);
        }

        return result;
    }

}
