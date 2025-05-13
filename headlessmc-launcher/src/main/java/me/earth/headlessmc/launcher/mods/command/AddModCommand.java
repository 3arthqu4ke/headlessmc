package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.mods.ModDistributionPlatform;
import me.earth.headlessmc.launcher.mods.ModdableGame;
import me.earth.headlessmc.launcher.mods.ModdableGameProvider;

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
        ModDistributionPlatform modDistributionPlatform = getPlatform(modName);
        try {
            modDistributionPlatform.download(game, modName);
            ctx.log("Downloaded mod " + modName + " from " + modDistributionPlatform.getName() + " successfully.");
        } catch (IOException e) {
            throw new CommandException("Failed to download " + modName + ": " + e.getMessage());
        }
    }

}
