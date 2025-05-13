package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.util.Table;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.api.VersionId;
import me.earth.headlessmc.launcher.mods.Mod;
import me.earth.headlessmc.launcher.mods.ModDistributionPlatform;
import me.earth.headlessmc.launcher.mods.ModdableGameProvider;

import java.io.IOException;
import java.util.List;

public class SearchModCommand extends AbstractModCommand {
    protected final ModdableGameProvider provider;

    public SearchModCommand(Launcher ctx, ModdableGameProvider provider) {
        super(ctx, "search", "Searches for a mod.");
        this.provider = provider;
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length <= 1) {
            throw new CommandException("Specify a mod to search for.");
        }

        String modName = args[1];
        ModDistributionPlatform modDistributionPlatform = getPlatform(args);
        try {
            List<Mod> mods;
            if (args.length > 2) {
                VersionId versionId = VersionId.parse(args[2]);
                mods = modDistributionPlatform.search(modName, versionId);
            } else {
                mods = modDistributionPlatform.search(modName);
            }

            ctx.log(new Table<Mod>()
                    .withColumn("name", Mod::getName)
                    .withColumn("description", Mod::getDescription)
                    .withColumn("authors", m -> String.join(", ", m.getAuthors()))
                    .addAll(mods)
                    .build());

        } catch (IOException e) {
            throw new CommandException(e.getMessage());
        }
    }

}
