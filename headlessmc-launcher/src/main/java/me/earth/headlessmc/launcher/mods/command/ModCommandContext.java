package me.earth.headlessmc.launcher.mods.command;

import me.earth.headlessmc.api.command.CommandContextImpl;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.mods.ModdableGameProvider;

public class ModCommandContext extends CommandContextImpl {
    public ModCommandContext(Launcher launcher, ModdableGameProvider provider) {
        super(launcher);
        add(new AddModCommand(launcher, provider));
        add(new RemoveModCommand(launcher, provider));
        add(new ListModCommand(launcher, provider));
        add(new SearchModCommand(launcher, provider));
    }

}
