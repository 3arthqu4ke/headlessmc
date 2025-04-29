package me.earth.headlessmc.launcher.server.commands;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.util.Table;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.launcher.server.Server;

public class ListServersCommand extends AbstractLauncherCommand {
    public ListServersCommand(Launcher ctx) {
        super(ctx, "list", "List installed servers.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        ctx.log(
            new Table<Server>()
                    .withColumn("id", server -> String.valueOf(server.getId()))
                    .withColumn("type", server -> server.getVersion().getServerType().getName())
                    .withColumn("version", server -> server.getVersion().getVersion())
                    .withColumn("name", HasName::getName)
                    .addAll(ctx.getServerManager().getContents())
                    .build()
        );
    }

}
