package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.mods.ModdableGame;
import me.earth.headlessmc.launcher.mods.ModdableGameProvider;
import me.earth.headlessmc.launcher.mods.files.ModFile;
import me.earth.headlessmc.launcher.mods.files.ModFileReadResult;

import java.io.IOException;
import java.nio.file.Files;

public class RemoveModCommand extends AbstractModdableGameCommand {
    public RemoveModCommand(Launcher ctx, ModdableGameProvider provider) {
        super(ctx, "remove", "Removes mods of a game.", provider);
    }

    @Override
    public void execute(ModdableGame game, String... args) throws CommandException {
        try {
            if (args.length <= 2) {
                throw new CommandException("Please specify a mod to delete.");
            }

            ModFileReadResult result = ctx.getModManager().getModFileReaderManager().read(game);
            if (result.getNonModFiles() > 0) {
                ctx.log(result.getNonModFiles() + " mod files could not be read and might be for other platforms.");
            }

            ModFile modFile = HasName.getByName(args[2], result.getMods());
            if (modFile == null) {
                modFile = HasId.getById(args[2], result.getMods());
                if (modFile == null) {
                    throw new CommandException("Mod '" + args[2] + "' not found in '" + game.getName() + "'.");
                }
            }

            for (ModFile otherMod : result.getMods()) {
                if (otherMod.getPath().equals(modFile.getPath()) && otherMod.getId() != modFile.getId()) {
                    ctx.log(otherMod.getName() + " will also be removed!");
                }
            }

            Files.deleteIfExists(modFile.getPath());
        } catch (IOException e) {
            throw new CommandException(e.getMessage());
        }
    }

}
