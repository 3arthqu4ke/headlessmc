package io.github.headlesshq.headlessmc.launcher.mods.command;

import io.github.headlesshq.headlessmc.api.command.CommandContextImpl;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.mods.ModdableGameProvider;

public class ModCommandContext extends CommandContextImpl {
    public ModCommandContext(Launcher launcher, ModdableGameProvider provider) {
        super(launcher);
        add(new AddModCommand(launcher, provider));
        add(new RemoveModCommand(launcher, provider));
        add(new ListModCommand(launcher, provider));
        add(new SearchModCommand(launcher, provider));
    }

}
