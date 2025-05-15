package io.github.headlesshq.headlessmc.api.command.impl;

import io.github.headlesshq.headlessmc.api.command.*;
import lombok.Getter;
import io.github.headlesshq.headlessmc.api.HasName;
import io.github.headlesshq.headlessmc.api.HeadlessMc;
import io.github.headlesshq.headlessmc.api.util.Table;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Getter
public class CommandContextCommand extends AbstractCommand {
    private final CommandContext commands;

    public CommandContextCommand(HeadlessMc ctx, String name, String description, CommandContext commands) {
        super(ctx, name, description);
        this.commands = commands;
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length <= 1) {
            ctx.log(
                    new Table<Command>()
                            .withColumn("command", HasName::getName)
                            .withColumn("description", HasDescription::getDescription)
                            .withColumn("args", HelpCommand::argsToString)
                            .addAll(getCommands())
                            .build()
            );

            return;
        }

        if (line.toLowerCase(Locale.ENGLISH).startsWith(getName().toLowerCase(Locale.ENGLISH) + " ")) {
            commands.execute(line.substring(getName().length() + 1));
        }
    }

    @Override
    public void getCompletions(String line, List<Map.Entry<String, @Nullable String>> completions, String... args) {
        super.getCompletions(line, completions, args);
        if (line.toLowerCase(Locale.ENGLISH).startsWith(getName().toLowerCase(Locale.ENGLISH) + " ")) {
            completions.addAll(commands.getCompletions(line.substring(getName().length() + 1)));
        }
    }

}
