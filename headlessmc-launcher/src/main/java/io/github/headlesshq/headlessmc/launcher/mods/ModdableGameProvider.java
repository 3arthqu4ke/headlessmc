package io.github.headlesshq.headlessmc.launcher.mods;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.api.VersionId;

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
