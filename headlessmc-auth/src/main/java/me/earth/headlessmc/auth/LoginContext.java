package me.earth.headlessmc.auth;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CommandContextImpl;

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
        log.getCommandLineManager().setCommandContext(commandContext);
        log.getCommandLineManager().setWaitingForInput(false);
    }

}
