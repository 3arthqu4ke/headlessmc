package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.util.Table;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.mods.Mod;
import me.earth.headlessmc.launcher.mods.ModdableGame;
import me.earth.headlessmc.launcher.mods.ModdableGameProvider;
import me.earth.headlessmc.launcher.mods.files.ModFile;
import me.earth.headlessmc.launcher.mods.files.ModFileReadResult;

import java.io.IOException;

public class ListModCommand extends AbstractModdableGameCommand {
    public ListModCommand(Launcher ctx, ModdableGameProvider provider) {
        super(ctx, "list", "Lists mods of a game.", provider);
    }

    @Override
    public void execute(ModdableGame game, String... args) throws CommandException {
        try {
            ModFileReadResult result = ctx.getModManager().getModFileReaderManager().read(game);
            if (result.getNonModFiles() > 0) {
                ctx.log(result.getNonModFiles() + " mod files could not be read and might be for other platforms.");
            }

            ctx.log(new Table<ModFile>()
                    .withColumn("name", Mod::getName)
                    .withColumn("description", Mod::getDescription)
                    .withColumn("authors", m -> String.join(", ", m.getAuthors()))
                    .addAll(result.getMods())
                    .build());
        } catch (IOException e) {
            throw new CommandException(e.getMessage());
        }
    }

}
