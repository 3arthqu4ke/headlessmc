package me.earth.headlessmc.launcher.command.login;

import lombok.CustomLog;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.AbstractCommand;

import java.util.function.Consumer;

@CustomLog
public class PasswordCommand extends AbstractCommand {
    private final CommandContext commandContext;
    private final Consumer<String> consumer;

    public PasswordCommand(HeadlessMc ctx,
                           CommandContext commandContext,
                           Consumer<String> consumer) {
        super(ctx, "password", "Logs you into the account.");
        this.commandContext = commandContext;
        this.consumer = consumer;
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("Please specify a password!");
        }

        if (args.length > 2) {
            log.warning("Found more than one arg, if you want to use a password"
                            + " containing spaces please escape it with \"");
        }

        ctx.log("Logging in...");
        consumer.accept(args[1]);
        ctx.setCommandContext(commandContext);
    }

}
