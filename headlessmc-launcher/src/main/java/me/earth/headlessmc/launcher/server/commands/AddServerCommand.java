package me.earth.headlessmc.launcher.server.commands;

import lombok.CustomLog;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.launcher.server.ServerType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

@CustomLog
public class AddServerCommand extends AbstractLauncherCommand {
    public AddServerCommand(Launcher ctx) {
        super(ctx, "add", "Add a server.");
        args.put("<type>", "The type of the server, e.g. paper, fabric or vanilla.");
        args.put("<version>", "The Mc version of the server.");
        args.put("<name>", "The name the server should have.");
        args.put("<type-version>", "A specific build of the type specified.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length <= 1) {
            throw new CommandException("Please specify the Server type, e.g. paper, fabric or vanilla");
        }

        ServerType serverType = ctx.getServerManager().getServerType(args[1]);
        if (serverType == null) {
            throw new CommandException("Server type " + args[1] + " not found. Available: " +
                    ctx.getServerManager().getServerTypes().stream().map(ServerType::getName)
                            .collect(Collectors.joining(",")));
        }

        String version = args.length > 2 ? args[2] : null;
        String name = args.length > 3 ? args[3] : null;
        String typeVersion = args.length > 4 ? args[4] : null;
        try {
            Path serverPath = ctx.getServerManager().add(ctx, serverType, name, version, typeVersion);
            ctx.log("Added " + serverType.getName() + " server: " + serverPath.getFileName().toString() + ".");
        } catch (IOException e) {
            log.info(e);
            throw new CommandException("Failed to add Server. " + e.getMessage());
        }
    }

}
