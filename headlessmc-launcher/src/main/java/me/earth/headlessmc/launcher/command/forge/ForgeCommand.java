package me.earth.headlessmc.launcher.command.forge;

import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractVersionCommand;
import me.earth.headlessmc.launcher.files.FileUtil;
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.util.Table;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@CustomLog
public class ForgeCommand extends AbstractVersionCommand {
    private final ForgeIndexCache cache;

    public ForgeCommand(Launcher ctx) {
        this(ctx, new ForgeIndexCache());
    }

    public ForgeCommand(Launcher ctx, ForgeIndexCache cache) {
        super(ctx, "forge", "Downloads forge.");
        args.put("<version>", "The version to download forge for.");
        args.put("--uid", "Specify a specific forge version.");
        args.put("-refresh", "Refresh index of forge versions.");
        args.put("-list", "List forge versions for the specified version.");
        this.cache = cache;
    }

    @Override
    public void execute(String... args) throws CommandException {
        boolean refresh = CommandUtil.hasFlag("-refresh", args);
        if (cache.isEmpty() || refresh) {
            cache.refresh();
        }

        if (args.length < 2 || args.length == 2 && refresh) {
            logTable(cache);
        } else {
            super.execute(args);
        }
    }

    @Override
    public void execute(Version ver, String... args) throws CommandException {
        val uid = CommandUtil.getOption("--uid", args);
        val versions = cache.stream()
                            .filter(v -> v.getVersion().equals(ver.getName()))
                            .filter(v -> uid == null
                                || uid.equalsIgnoreCase(v.getName()))
                            .collect(Collectors.toList());

        if (CommandUtil.hasFlag("-list", args)) {
            logTable(versions);
            return;
        }

        val version = versions.stream()
                              .findFirst()
                              .orElseThrow(() -> new CommandException(
                                  "Couldn't find Forge for version "
                                      + ver.getName() + (uid == null
                                      ? "!"
                                      : " and uid " + uid + "!")));

        ctx.log("Installing Forge " + version.getFullName());
        val uuid = UUID.randomUUID();
        val fm = ctx.getFileManager().createRelative(uuid.toString());
        try {
            new ForgeInstaller(ctx).install(version, fm);
            ctx.getVersionService().refresh();
        } catch (IOException e) {
            val message = "Failed to install forge for version " + ver.getName()
                + ": " + e.getMessage();
            log.error(message);
            throw new CommandException(message);
        } finally {
            try {
                FileUtil.delete(fm.getBase());
            } catch (IOException e) {
                log.error("Couldn't delete " + fm.getBase() + ": " + e);
            }
        }
    }

    private void logTable(Iterable<ForgeVersion> versions) {
        ctx.log("Forge versions:");
        ctx.log(new Table<ForgeVersion>()
                    .addAll(versions)
                    .withColumn("version", ForgeVersion::getVersion)
                    .withColumn("uid", ForgeVersion::getName)
                    .build());
    }

}
