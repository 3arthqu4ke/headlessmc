package io.github.headlesshq.headlessmc.launcher.mods.command;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.api.util.Table;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.api.VersionId;
import io.github.headlesshq.headlessmc.launcher.mods.Mod;
import io.github.headlesshq.headlessmc.launcher.mods.ModDistributionPlatform;
import io.github.headlesshq.headlessmc.launcher.mods.ModdableGameProvider;

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
