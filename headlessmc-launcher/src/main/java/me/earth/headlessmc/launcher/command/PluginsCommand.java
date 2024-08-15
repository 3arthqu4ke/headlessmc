package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.HasDescription;
import me.earth.headlessmc.api.util.Table;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.plugin.HeadlessMcPlugin;

public class PluginsCommand extends AbstractLauncherCommand {
    public PluginsCommand(Launcher ctx) {
        super(ctx, "plugins", "Lists installed plugins.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        // TODO: list TransformerPlugins
        ctx.log(new Table<HeadlessMcPlugin>()
            .withColumn("name", HasName::getName)
            .withColumn("description", HasDescription::getDescription)
            .addAll(ctx.getPluginManager().getPlugins())
            .build());
    }

}
