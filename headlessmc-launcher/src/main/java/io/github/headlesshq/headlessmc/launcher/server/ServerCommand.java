package io.github.headlesshq.headlessmc.launcher.server;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.command.AbstractLauncherCommandCtxCommand;
import io.github.headlesshq.headlessmc.launcher.server.commands.ServerCommandContext;

import java.util.Locale;

public class ServerCommand extends AbstractLauncherCommandCtxCommand {
    public ServerCommand(Launcher ctx) {
        super(ctx, "server", "Configure Servers with HeadlessMc.",
                new ServerCommandContext(ctx, false));
        args.put("[add/launch/remove/list/eula/mod/rename]", "Action to perform.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length <= 1) {
            super.execute(line, args);
            return;
        }

        if (line.toLowerCase(Locale.ENGLISH).startsWith("server mode")) {
            ctx.getCommandLine().setAllContexts(new ServerCommandContext(ctx, true));
        } else if (line.toLowerCase(Locale.ENGLISH).startsWith("server ")) {
            getCommands().execute(line.substring("server ".length()));
        }
    }

}
