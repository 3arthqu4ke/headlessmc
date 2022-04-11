package me.earth.headlessmc.command;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.HasDescription;
import me.earth.headlessmc.util.Table;

public class HelpCommand extends AbstractCommand {
    public HelpCommand(HeadlessMc ctx) {
        super(ctx, "help", "Information about commands.");
    }

    @Override
    public void execute(String... args) {
        if (args.length > 1) {
            Command cmd = findCommand(args[1]);
            if (cmd != null) {
                if (args.length > 2) {
                    String desc = cmd.getArgDescription(args[2]);
                    if (desc == null) {
                        ctx.log("");
                    } else {
                        ctx.log(String.format(
                            "%s %s: %s", cmd.getName(), args[2], desc));
                    }
                } else {
                    ctx.log(String.format("%s : %s", cmd.getName(),
                                                   cmd.getDescription()));
                }
            } else {
                ctx.log(
                    String.format("Couldn't find command %s", name));
            }
        } else {
            ctx.log(
                new Table<Command>()
                    .withColumn("command", HasName::getName)
                    .withColumn("description", HasDescription::getDescription)
                    .addAll(ctx.getCommandContext())
                    .build());
        }
    }

    private Command findCommand(String name) {
        for (Command cmd : ctx.getCommandContext()) {
            if (name.equalsIgnoreCase(cmd.getName())) {
                return cmd;
            }
        }

        return null;
    }

}
