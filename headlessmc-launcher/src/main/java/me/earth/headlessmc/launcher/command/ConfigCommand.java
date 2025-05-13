package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.api.command.FindByCommand;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.PropertyTypes;
import me.earth.headlessmc.api.util.Table;
import me.earth.headlessmc.launcher.Launcher;

// TODO: this!
public class ConfigCommand extends AbstractLauncherCommand implements FindByCommand<Config> {
    public ConfigCommand(Launcher ctx) {
        super(ctx, "config", "Manage your configs.");
        args.put("-refresh", "Reloads all configs from the disk.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        String property = CommandUtil.getOption("--property", args);
        if (property != null) {
            if (property.contains("=")) {
                String[] split = property.split("=", 2);
                String value = split.length > 1 ? split[1] : "";
                System.setProperty(split[0], value);
                ctx.log("Set property " + split[0] + " to " + value);
            } else {
                ctx.log(property + " = " + ctx.getConfig().get(PropertyTypes.string(property)));
            }

            return;
        }

        if (CommandUtil.hasFlag("-refresh", args)) {
            ctx.getConfigService().refresh();
        }

        if (args.length == 1) {
            ctx.log(new Table<Config>()
                        .withColumn("id", c -> String.valueOf(c.getId()))
                        .withColumn("name", HasName::getName)
                        .addAll(ctx.getConfigService())
                        .build());
        } else if (!(args.length > 1 && args[1].equalsIgnoreCase("-refresh"))) {
            FindByCommand.super.execute(line, args);
        }
    }

    @Override
    public void execute(Config obj, String... args) {
        ctx.log("Loading config " + obj.getName() + "...");
        ctx.getConfigService().setConfig(obj);
    }

    @Override
    public Iterable<Config> getIterable() {
        return ctx.getConfigService();
    }

}
