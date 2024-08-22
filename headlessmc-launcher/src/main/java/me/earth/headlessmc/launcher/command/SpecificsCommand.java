package me.earth.headlessmc.launcher.command;

import lombok.CustomLog;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.util.Table;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.specifics.VersionSpecificModRepository;
import me.earth.headlessmc.launcher.version.Version;

import java.io.IOException;
import java.util.stream.Collectors;

@CustomLog
public class SpecificsCommand extends AbstractVersionCommand {
    public SpecificsCommand(Launcher ctx) {
        super(ctx, "specifics", "Installs version specific mods.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length < 2) {
            ctx.log(new Table<VersionSpecificModRepository>()
                        .withColumn("name", VersionSpecificModRepository::getName)
                        .withColumn("version", VersionSpecificModRepository::getVersion)
                        .withColumn("url", v -> v.getUrl().toString())
                        .addAll(ctx.getVersionSpecificModManager().getSpecificMods())
                        .build());
            return;
        }

        super.execute(line, args);
    }

    @Override
    public void execute(Version version, String... args) throws CommandException {
        if (args.length < 3) {
            String list = ctx.getVersionSpecificModManager().getSpecificMods().stream().map(HasName::getName).collect(Collectors.joining(", "));
            throw new CommandException("Please specify which version specific mod to download (" + list + ").");
        }

        VersionSpecificModRepository repository = ctx.getVersionSpecificModManager().getRepository(args[2]);

        try {
            ctx.getVersionSpecificModManager().download(version, repository);
            ctx.getVersionSpecificModManager().deleteSpecificsOfOtherVersions(version, repository, ctx.getGameDir().getDir("mods").toPath());
            ctx.getVersionSpecificModManager().install(version, repository, ctx.getGameDir().getDir("mods").toPath());
        } catch (IOException e) {
            log.debug("Failed to install " + repository.getName() + " for version " + version.getName(), e);
            throw new CommandException("Failed to install " + repository.getName() + " for version " + version.getName() + ": " + e.getMessage());
        }
    }

}
