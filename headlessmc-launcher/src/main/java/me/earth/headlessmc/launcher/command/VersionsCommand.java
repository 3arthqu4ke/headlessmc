package me.earth.headlessmc.launcher.command;

import lombok.val;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.version.VersionUtil;

public class VersionsCommand extends AbstractLauncherCommand {
    public VersionsCommand(Launcher launcher) {
        super(launcher, "versions", "Lists all Minecraft versions.");
        args.putAll(VersionTypeFilter.getArgs());
    }

    @Override
    public void execute(String... args) {
        if (CommandUtil.hasFlag("-refresh", args)) {
            ctx.getVersionService().refresh();
        }

        val filter = VersionTypeFilter.forVersions();
        val versions = filter.apply(ctx.getVersionService(), args);
        ctx.log(VersionUtil.makeTable(versions));
    }

}
