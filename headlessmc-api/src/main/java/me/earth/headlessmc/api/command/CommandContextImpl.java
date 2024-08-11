package me.earth.headlessmc.api.command;

import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.config.HmcProperties;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
            if (cmd.matches(message, args)) {
                executeCommand(cmd, message, args);
                notFound = false;
                break;
            }
        }

        if (notFound) {
            fail(args);
        }
    }

    protected void executeCommand(Command cmd, String message, String... args) {
        try {
            cmd.execute(message, args);
        } catch (CommandException commandException) {
            log.log(commandException.getMessage());
            if (log.getConfig().get(HmcProperties.EXIT_ON_FAILED_COMMAND, false)) {
                log.getExitManager().exit(-1);
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
                    c -> -CommandUtil.levenshtein(c.getName(), args[0])))
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
            log.getExitManager().exit(-1);
        }
    }

    @Override
    public @NotNull Iterator<Command> iterator() {
        return commands.iterator();
    }

}
