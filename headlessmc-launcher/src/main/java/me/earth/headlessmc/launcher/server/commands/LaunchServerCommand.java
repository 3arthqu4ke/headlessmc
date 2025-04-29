package me.earth.headlessmc.launcher.server.commands;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLaunchProcessLifecycle;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.launcher.command.FindByCommand;
import me.earth.headlessmc.launcher.launch.LaunchException;
import me.earth.headlessmc.launcher.server.Server;
import me.earth.headlessmc.launcher.server.ServerLauncher;

import java.nio.file.Path;

@CustomLog
public class LaunchServerCommand extends AbstractLauncherCommand implements FindByCommand<Server> {
    public LaunchServerCommand(Launcher ctx) {
        super(ctx, "launch", "Start a server.");
    }

    @Override
    public void execute(Server server, String... args) throws CommandException {
        ServerLaunchProcessLifecycle lifecycle = new ServerLaunchProcessLifecycle(server);
        lifecycle.run(server, args);
    }

    @Override
    public Iterable<Server> getIterable() {
        return ctx.getServerManager();
    }

    private class ServerLaunchProcessLifecycle extends AbstractLaunchProcessLifecycle {
        private final ServerLauncher serverLauncher;
        private final Server server;

        public ServerLaunchProcessLifecycle(Server server) {
            super(LaunchServerCommand.this.ctx);
            this.serverLauncher = new ServerLauncher(ctx, server);
            this.server = server;
        }

        @Override
        protected Path getGameDir() {
            return server.getPath();
        }

        @Override
        protected Process createProcess() throws LaunchException {
            try {
                return serverLauncher.launch();
            } catch (CommandException e) {
                throw new LaunchException(e);
            }
        }
    }

}
