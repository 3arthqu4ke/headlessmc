package me.earth.headlessmc.command;

import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.Command;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.config.HmcProperties;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.earth.headlessmc.command.CommandUtil.levenshtein;

@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "RedundantSuppression"}) // delegate
public class CommandContextImpl implements CommandContext {
    protected final List<Command> commands = new ArrayList<>();
    protected final HeadlessMc log;

    @Override
    public void execute(String message) {
        val args = CommandUtil.split(message);
        boolean notFound = true;
        for (val cmd : this) {
            if (cmd.matches(args)) {
                executeCommand(cmd, args);
                notFound = false;
                break;
            }
        }

        if (notFound) {
            fail(args);
        }
    }

    protected void executeCommand(Command cmd, String... args) {
        try {
            cmd.execute(args);
        } catch (CommandException commandException) {
            log.log(commandException.getMessage());
            if (log.getConfig().get(HmcProperties.EXIT_ON_FAILED_COMMAND, false)) {
                System.exit(-1);
            }
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

        if (log.getConfig().get(HmcProperties.EXIT_ON_FAILED_COMMAND, false)) {
            System.exit(-1);
        }
    }

    @Override
    public @NotNull Iterator<Command> iterator() {
        return commands.iterator();
    }

}
