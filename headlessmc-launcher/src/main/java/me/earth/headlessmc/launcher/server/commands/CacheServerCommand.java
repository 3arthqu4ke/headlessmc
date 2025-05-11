package me.earth.headlessmc.launcher.server.commands;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.api.command.FindByCommand;
import me.earth.headlessmc.launcher.server.Server;

import java.io.IOException;

@CustomLog
public class CacheServerCommand extends AbstractLauncherCommand implements FindByCommand<Server> {
    public CacheServerCommand(Launcher ctx) {
        super(ctx, "cache", "Cache servers for reuse.");
        args.put("<server>", "The server to cache.");
    }

    @Override
    public void execute(Server server, String... args) throws CommandException {
        try {
            ctx.getServerManager().cache(ctx, server);
            ctx.log("Cached server '" + server.getName() + "'.");
        } catch (IOException e) {
            log.error("Failed to cache server " + server.getPath(), e);
            throw new CommandException(e);
        }
    }

    @Override
    public Iterable<Server> getIterable() {
        return ctx.getServerManager();
    }

}
