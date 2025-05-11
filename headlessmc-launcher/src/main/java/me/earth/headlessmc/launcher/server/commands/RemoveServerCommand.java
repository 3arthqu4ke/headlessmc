package me.earth.headlessmc.launcher.server.commands;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.api.command.FindByCommand;
import me.earth.headlessmc.launcher.server.Server;

import java.io.IOException;

@CustomLog
public class RemoveServerCommand extends AbstractLauncherCommand implements FindByCommand<Server> {
    public RemoveServerCommand(Launcher ctx) {
        super(ctx, "remove", "Remove a server.");
    }

    @Override
    public void execute(Server server, String... args) throws CommandException {
        try {
            ctx.getServerManager().remove(server);
            ctx.log(String.format("Removed server '%s'.", server.getName()));
        } catch (IOException e) {
            log.error(e);
            throw new CommandException(e);
        }
    }

    @Override
    public Iterable<Server> getIterable() {
        return ctx.getServerManager();
    }

}
