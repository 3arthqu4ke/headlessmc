package me.earth.headlessmc.command;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CommandException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static me.earth.headlessmc.command.CommandUtil.levenshtein;

@RequiredArgsConstructor
public class CommandContextImpl implements CommandContext {
    @Delegate(types = Iterable.class)
    protected final List<Command> commands = new ArrayList<>();
    protected final HeadlessMc log;

    @Override
    public void execute(String message) {
        val partCommands = CommandUtil.split(message);
        for (val partCommand : partCommands) {
            boolean notFound = true;
            for (val cmd : this) {
                if (cmd.matches(partCommand)) {
                    executeCommand(cmd, partCommand);
                    notFound = false;
                    break;
                }
            }

            if (notFound) {
                fail(partCommand);
                return;
            }
        }
    }

    protected void executeCommand(Command cmd, String... args) {
        try {
            cmd.execute(args);
        } catch (CommandException commandException) {
            log.log(commandException.getMessage());
        }
    }

    protected void add(Command command) {
        commands.add(command);
    }

    protected void fail(String... args) {
        if (args.length == 0) {
            log.log("Please enter a command...");
        } else {
            Command command = commands
                .stream()
                .max(Comparator.comparingInt(
                    c -> -levenshtein(c.getName(), args[0])))
                .orElse(null);

            if (command == null) {
                log.log("No commands are available right now.");
            } else {
                log.log(
                    String.format(
                        "Couldn't find command for '%s', did you mean '%s'?",
                        Arrays.toString(args), command.getName()));
            }
        }
    }

}
