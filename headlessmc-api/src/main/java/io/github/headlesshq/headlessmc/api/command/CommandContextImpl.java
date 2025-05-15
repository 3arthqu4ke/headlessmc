package io.github.headlesshq.headlessmc.api.command;

import lombok.RequiredArgsConstructor;
import lombok.val;
import io.github.headlesshq.headlessmc.api.HeadlessMc;
import io.github.headlesshq.headlessmc.api.LogsMessages;
import io.github.headlesshq.headlessmc.api.config.HmcProperties;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Default implementation of {@link CommandContext}.
 */
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "RedundantSuppression"}) // delegate
public class CommandContextImpl implements CommandContext {
    /**
     * The list of Commands that this CommandContext can execute.
     */
    protected final List<Command> commands = new ArrayList<>();
    /**
     * The HeadlessMc instance this CommandContext uses for {@link LogsMessages#log(String)}.
     */
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

    /**
     * Calls {@link Command#execute(String, String...)} on the given command for the given Arguments.
     * Catches any {@link CommandException} that might occur and logs it.
     *
     * @param cmd the command to execute.
     * @param message the full command.
     * @param args the message split into arguments.
     */
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
