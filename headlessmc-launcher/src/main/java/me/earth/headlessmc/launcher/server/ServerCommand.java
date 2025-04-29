package me.earth.headlessmc.launcher.server;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.HasDescription;
import me.earth.headlessmc.api.command.impl.HelpCommand;
import me.earth.headlessmc.api.util.Table;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;
import me.earth.headlessmc.launcher.server.commands.ServerCommandContext;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ServerCommand extends AbstractLauncherCommand {
    private final ServerCommandContext commandContext;

    public ServerCommand(Launcher ctx) {
        super(ctx, "server", "Configure Servers with HeadlessMc.");
        this.commandContext = new ServerCommandContext(ctx, false);
        args.put("[add/launch/remove/list/eula/rename]", "Action to perform.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length <= 1) {
            ctx.log(
                    new Table<Command>()
                            .withColumn("command", HasName::getName)
                            .withColumn("description", HasDescription::getDescription)
                            .withColumn("args", HelpCommand::argsToString)
                            .addAll(commandContext)
                            .build()
            );

            return;
        }

        if (line.toLowerCase(Locale.ENGLISH).startsWith("server mode")) {
            ctx.getCommandLine().setAllContexts(new ServerCommandContext(ctx, true));
        } else if (line.toLowerCase(Locale.ENGLISH).startsWith("server ")) {
            commandContext.execute(line.substring("server ".length()));
        }
    }

    @Override
    public void getCompletions(String line, List<Map.Entry<String, @Nullable String>> completions, String... args) {
        if (args.length >= 1 && line.toLowerCase(Locale.ENGLISH).startsWith("server ")) {
            commandContext.getCompletions(line.substring("server ".length()));
        }

        super.getCompletions(line, completions, args);
    }

}
