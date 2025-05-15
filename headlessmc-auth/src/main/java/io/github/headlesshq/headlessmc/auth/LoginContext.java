package io.github.headlesshq.headlessmc.auth;

import io.github.headlesshq.headlessmc.api.HeadlessMc;
import io.github.headlesshq.headlessmc.api.command.CommandContext;
import io.github.headlesshq.headlessmc.api.command.CommandContextImpl;

import java.util.Locale;

public class LoginContext extends CommandContextImpl {
    protected final CommandContext commandContext;
    protected final String helpMessage;

    public LoginContext(HeadlessMc ctx, CommandContext commandContext, String helpMessage) {
        super(ctx);
        this.commandContext = commandContext;
        this.helpMessage = helpMessage;
    }

    @Override
    public void execute(String message) {
        String lower = message.toLowerCase(Locale.ENGLISH);
        if (lower.equalsIgnoreCase("abort")) {
            log.log("Aborting login process.");
            returnToPreviousContext();
        } else if (lower.equalsIgnoreCase("help")) {
            log.log(helpMessage);
        } else {
            onCommand(message);
        }
    }

    protected void onCommand(String message) {
        // to be implemented by sub classes
    }

    protected void returnToPreviousContext() {
        log.getCommandLine().setCommandContext(commandContext);
        log.getCommandLine().setWaitingForInput(false);
    }

}
