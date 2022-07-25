package me.earth.headlessmc.launcher.command.download;

import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.command.YesNoContext;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.launcher.command.FindByCommand;
import me.earth.headlessmc.launcher.command.VersionTypeFilter;
import me.earth.headlessmc.launcher.util.IOUtil;
import me.earth.headlessmc.util.Table;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@CustomLog
public class DownloadCommand extends AbstractLauncherCommand
    implements FindByCommand<VersionInfo> {
    private final VersionInfoCache cache = new VersionInfoCache();

    public DownloadCommand(Launcher ctx) {
        super(ctx, "download", "Downloads a version.");
        args.put("<version/id>", "The name/id of the version to download." +
            " If you use the id you also need to use the -id flag.");
        args.put("-id", "If you specified the version via id you" +
            " need to add this flag.");
        args.putAll(VersionTypeFilter.getArgs());
    }

    @Override
    public void execute(VersionInfo version, String... args)
        throws CommandException {
        val file = new File(ctx.getMcFiles().getDir("versions") + File.separator
                                + version.getName() + File.separator
                                + version.getName() + ".json");
        if (file.exists()) {
            ctx.log(version.getName() + " has already been downloaded," +
                        " download anyways? (y/n)");
            YesNoContext.goBackAfter(ctx, r -> {
                if (r) {
                    download(version, file);
                }
            });
        } else {
            download(version, file);
        }
    }

    private void download(VersionInfo version, File file)
        throws CommandException {
        ctx.log("Downloading " + version.getName() + "...");
        try {
            log.debug("Downloading version " + version.getName() + " from "
                          + version.getUrl() + " to " + file.getAbsolutePath());
            IOUtil.download(version.getUrl(), file.getAbsolutePath());
            ctx.getVersionService().refresh();
            ctx.log("Download successful!");
        } catch (IOException ioe) {
            throw new CommandException("Couldn't download version "
                                           + version.getName() + " from "
                                           + version.getUrl() + ": "
                                           + ioe.getMessage());
        }
    }

    @Override
    public void execute(String... args) throws CommandException {
        /*
            !!!

            Don't use lomboks val inside method. Compilation will fail with an
            AssertionError. This bug seems to be exclusive to crosscompiling so
            I didn't report it. The cause seems to be the 'FindByCommand.super'.

            !!!
         */
        Collection<VersionInfo> versions =
            cache.cache(CommandUtil.hasFlag("-refresh", args));
        if (args.length < 2 || super.args.containsKey(args[1].toLowerCase())) {
            Collection<VersionInfo> filtered =
                new VersionTypeFilter<>(VersionInfo::getType)
                    .apply(versions, args);

            ctx.log(new Table<VersionInfo>()
                        .addAll(filtered)
                        .withColumn("id", v -> String.valueOf(v.getId()))
                        .withColumn("name", VersionInfo::getName)
                        .withColumn("type", VersionInfo::getType)
                        .build());
        } else {
            FindByCommand.super.execute(args);
        }
    }

    @Override
    public Iterable<VersionInfo> getIterable() {
        return cache;
    }

}
