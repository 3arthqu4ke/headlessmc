package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.FindByCommand;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.api.VersionId;
import me.earth.headlessmc.launcher.mods.ModdableGame;
import me.earth.headlessmc.launcher.mods.ModdableGameProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public abstract class AbstractModdableGameCommand extends AbstractModCommand implements FindByCommand<ModdableGame> {
    protected final ModdableGameProvider provider;

    public AbstractModdableGameCommand(Launcher ctx, String name, String desc, ModdableGameProvider provider) {
        super(ctx, name, desc);
        this.provider = provider;
    }

    @Override
    public @Nullable ModdableGame findObject(boolean byId, boolean byRegex, String versionArg, String... args) throws CommandException {
        ModdableGame result = FindByCommand.super.findObject(byId, byRegex, versionArg, args);
        if (result == null) {
            VersionId versionArgument = VersionId.parse(versionArg);
            return find(versionArgument, getIterable());
        }

        return result;
    }

    @Override
    public Iterable<ModdableGame> getIterable() {
        return provider.getGames();
    }

    // completions for new version format?

    // for testing
    protected ModdableGame find(VersionId versionArgument, Iterable<ModdableGame> versions) {
        String mlVersion = versionArgument.getBuild() == null ? null : versionArgument.getBuild().toLowerCase(Locale.ENGLISH);
        for (ModdableGame game : versions) {
            if (game.getPlatform() == versionArgument.getPlatform()
                    && (mlVersion == null
                        || game.getBuild() != null && game.getBuild().equalsIgnoreCase(mlVersion)
                        || game.getName().toLowerCase(Locale.ENGLISH).contains(mlVersion))
                    && (game.getVersionName().equalsIgnoreCase(versionArgument.getName()))) {
                return game;
            }
        }

        return null;
    }

}
