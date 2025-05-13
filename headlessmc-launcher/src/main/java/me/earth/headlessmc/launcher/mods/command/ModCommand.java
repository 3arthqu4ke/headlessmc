package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommandCtxCommand;
import me.earth.headlessmc.launcher.mods.ModdableGameProvider;
import me.earth.headlessmc.launcher.mods.VersionModdableGameProvider;

public class ModCommand extends AbstractLauncherCommandCtxCommand {
    public ModCommand(Launcher ctx, ModdableGameProvider moddableGameProvider) {
        super(ctx, "mod", "Configure mods with HeadlessMc.",
                new ModCommandContext(ctx, moddableGameProvider));
        args.put("[add/remove/list/search]", "Action to perform.");
    }

    public static ModCommand forClientVersions(Launcher ctx) {
        return new ModCommand(ctx, new VersionModdableGameProvider(ctx.getVersionService(), ctx));
    }

}