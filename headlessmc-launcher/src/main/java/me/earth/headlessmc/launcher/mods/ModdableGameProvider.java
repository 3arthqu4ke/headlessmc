package me.earth.headlessmc.launcher.mods;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.api.VersionId;

public interface ModdableGameProvider {
    Iterable<ModdableGame> getGames();

    void download(Launcher launcher, VersionId version, String... args) throws CommandException;

    default boolean providesOnlyClients() {
        return false;
    }

    default boolean providesOnlyServers() {
        return false;
    }

}
