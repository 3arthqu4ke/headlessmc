package me.earth.headlessmc.api.command.impl;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.*;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.util.Table;

import java.util.Map;

/**
 * A {@link Command} implementation that displays information about Commands from a {@link CommandContext}.
 */
public class HelpCommand extends AbstractCommand {
    /**
     * Constructs a new HelpCommand.
     * The {@link CommandContext} from the {@link CommandLine} of the given {@link HeadlessMc}
     * will be used to list commands and to find commands to display information for.
     *
     * @param ctx the HeadlessMc instance holding the CommandLine with the commands.
     */
    public HelpCommand(HeadlessMc ctx) {
        super(ctx, "help", "Information about commands.");
        args.put("<command>", "The name of the command to get help for.");
        args.put("<arg>", "The name of the argument to get help for.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length > 1) {
            Command cmd = findCommand(args[1]);
            if (cmd != null) {
                if (args.length > 2) {
                    String desc = cmd.getArgDescription(args[2]);
                    if (desc == null) {
                        throw new CommandException("No description found for '" + args[2] + "'.");
                    } else {
                        ctx.log(String.format("%s %s: %s", cmd.getName(), args[2], desc));
                    }
                } else {
                    ctx.log(String.format("%s : %s", cmd.getName(), cmd.getDescription()));
                    ctx.log(
                        new Table<Map.Entry<String, String>>()
                            .withColumn("arg", Map.Entry::getKey)
                            .withColumn("description", Map.Entry::getValue)
                            .addAll(cmd.getArgs2Descriptions())
                            .build()
                    );
                }
            } else {
                throw new CommandException(String.format("Couldn't find command %s", args[1]));
            }
        } else {
            ctx.log(
                new Table<Command>()
                    .withColumn("command", HasName::getName)
                    .withColumn("description", HasDescription::getDescription)
                    .withColumn("args", this::argsToString)
                    .addAll(ctx.getCommandLine().getCommandContext())
                    .build()
            );
        }
    }

    private Command findCommand(String name) {
        for (Command cmd : ctx.getCommandLine().getCommandContext()) {
            if (name.equalsIgnoreCase(cmd.getName())) {
                return cmd;
            }
        }

        return null;
    }

    private String argsToString(Command command) {
        StringBuilder sb = new StringBuilder();
        for (String arg : command.getArgs()) {
            sb.append(arg).append(" ");
        }

        return sb.toString();
    }

}
