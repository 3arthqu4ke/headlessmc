package io.github.headlesshq.headlessmc.launcher.mods.command;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.api.command.CommandUtil;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.LauncherProperties;
import io.github.headlesshq.headlessmc.launcher.api.VersionId;
import io.github.headlesshq.headlessmc.launcher.mods.ModdableGame;
import io.github.headlesshq.headlessmc.launcher.mods.ModdableGameProvider;
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
