package me.earth.headlessmc.launcher.server.commands;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.api.command.FindByCommand;
import me.earth.headlessmc.launcher.launch.LaunchException;
import me.earth.headlessmc.launcher.server.Server;
import me.earth.headlessmc.launcher.server.ServerLauncher;

import java.io.IOException;

@CustomLog
public class EulaCommand extends AbstractLauncherCommand implements FindByCommand<Server> {
    public EulaCommand(Launcher ctx) {
        super(ctx, "eula", "Handle the EULA of a server.");
        args.put("<server>", "The server to handle.");
        args.put("accept", "If you want to accept the EULA.");
    }

    @Override
    public void execute(Server server, String... args) throws CommandException {
        try {
            ServerLauncher serverLauncher = new ServerLauncher(ctx, server, args);
            serverLauncher.eulaLaunch();

            if (args.length > 1 && (
                    CommandUtil.hasFlag("accept", args)
                        || CommandUtil.hasFlag("-accept", args)
                        || CommandUtil.hasFlag("--accept", args))) {
                serverLauncher.acceptEula();
                ctx.log("EULA accepted.");
            } else {
                ctx.log(serverLauncher.readEula());
            }
        } catch (IOException | LaunchException e) {
            log.error(e);
            throw new CommandException("Failed to read EULA of server " + server.getName(), e);
        }
    }

    @Override
    public Iterable<Server> getIterable() {
        return ctx.getServerManager();
    }

}
