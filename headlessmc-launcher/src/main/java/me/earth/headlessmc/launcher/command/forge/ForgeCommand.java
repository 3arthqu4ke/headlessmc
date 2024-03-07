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
    private final ForgeInstaller installer;
    private final ForgeIndexCache cache;

    public ForgeCommand(Launcher ctx, String name, ForgeInstaller installer, ForgeIndexCache cache) {
        super(ctx, name, "Downloads " + name + ".");
        args.put("<version>", "The version to download " + name + " for.");
        args.put("--uid", "Specify a specific " + name + " version.");
        args.put("-refresh", "Refresh index of " + name + " versions.");
        args.put("-list", "List " + name + " versions for the specified version.");
        this.installer = installer;
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
            installer.install(version, fm);
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

    public static ForgeCommand lexforge(Launcher launcher) {
        ForgeInstaller installer = new ForgeInstaller(ForgeRepoFormat.lexForge(), launcher, "Forge", ForgeRepoFormat.LEX_FORGE_URL);
        return new ForgeCommand(launcher, "forge", installer, new ForgeIndexCache(ForgeIndexCache.LEX_FORGE_INDICES));
    }

    public static ForgeCommand neoforge(Launcher launcher) {
        ForgeInstaller installer = new ForgeInstaller(ForgeRepoFormat.neoForge(), launcher, "NeoForge", ForgeRepoFormat.NEO_FORGE_URL);
        return new ForgeCommand(launcher, "neoforge", installer, new ForgeIndexCache(ForgeIndexCache.NEO_FORGE_INDICES));
    }

}
