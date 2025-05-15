package io.github.headlesshq.headlessmc.launcher.mods.command;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.mods.ModDistributionPlatform;
import io.github.headlesshq.headlessmc.launcher.mods.ModdableGame;
import io.github.headlesshq.headlessmc.launcher.mods.ModdableGameProvider;

import java.io.IOException;

public class AddModCommand extends AbstractDownloadingModdableGameCommand {
    public AddModCommand(Launcher ctx, ModdableGameProvider provider) {
        super(ctx, "add", "Adds mods to the game.", provider);
    }

    @Override
    public void execute(ModdableGame game, String... args) throws CommandException {
        if (args.length <= 2) {
            throw new CommandException("Please specify a mod to add to " + game.getName());
        }

        String modName = args[2];
        ModDistributionPlatform modDistributionPlatform = getPlatform(args);
        try {
            modDistributionPlatform.download(game, modName);
            ctx.log("Downloaded mod " + modName + " from " + modDistributionPlatform.getName() + " successfully.");
        } catch (IOException e) {
            throw new CommandException("Failed to download " + modName + ": " + e.getMessage());
        }
    }

}
