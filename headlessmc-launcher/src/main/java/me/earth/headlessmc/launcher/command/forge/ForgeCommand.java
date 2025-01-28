package me.earth.headlessmc.launcher.command.forge;

import lombok.CustomLog;
import lombok.Getter;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.api.util.Table;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.command.AbstractVersionCommand;
import me.earth.headlessmc.launcher.command.download.AbstractDownloadingVersionCommand;
import me.earth.headlessmc.launcher.command.download.ModLauncherCommand;
import me.earth.headlessmc.launcher.version.Version;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@CustomLog
public class ForgeCommand extends AbstractDownloadingVersionCommand implements ModLauncherCommand {
    private final ForgeInstaller installer;
    private final ForgeIndexCache cache;

    public ForgeCommand(Launcher ctx, String name, ForgeInstaller installer, ForgeIndexCache cache) {
        super(ctx, name, "Downloads " + name + ".");
        args.put("<version>", "The version to download " + name + " for.");
        args.put("--uid", "Specify a specific " + name + " version.");
        args.put("-refresh", "Refresh index of " + name + " versions.");
        args.put("-list", "List " + name + " versions for the specified version.");
        args.put("-inmemory", "Launch the forge installer inside this JVM.");
        this.installer = installer;
        this.cache = cache;
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        boolean refresh = CommandUtil.hasFlag("-refresh", args);
        if (cache.isEmpty() || refresh) {
            cache.refresh();
        }

        if (args.length < 2 || args.length == 2 && refresh) {
            logTable(cache);
        } else {
            super.execute(line, args);
        }
    }

    @Override
    public void execute(Version ver, String... args) throws CommandException {
        val uid = CommandUtil.getOption("--uid", args);
        if (cache.isEmpty() || CommandUtil.hasFlag("-norecursivedownload", args)) {
            cache.refresh();
        }

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
            installer.install(version, fm, CommandUtil.hasFlag("-inmemory", args) || ctx.getConfig().get(LauncherProperties.ALWAYS_IN_MEMORY, false));
            ctx.getVersionService().refresh();
        } catch (IOException e) {
            val message = "Failed to install forge for version " + ver.getName()
                + ": " + e.getMessage();
            log.error(message);
            throw new CommandException(message);
        } finally {
            try {
                ctx.getFileManager().delete(fm.getBase());
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
        return new ForgeCommand(launcher, "forge", installer, new ForgeIndexCache(launcher, ForgeIndexCache.LEX_FORGE_INDICES));
    }

    public static ForgeCommand neoforge(Launcher launcher) {
        ForgeInstaller installer = new ForgeInstaller(ForgeRepoFormat.neoForge(), launcher, "NeoForge", ForgeRepoFormat.NEO_FORGE_URL);
        return new ForgeCommand(launcher, "neoforge", installer, new ForgeIndexCache(launcher, ForgeIndexCache.NEO_FORGE_INDICES));
    }

}
