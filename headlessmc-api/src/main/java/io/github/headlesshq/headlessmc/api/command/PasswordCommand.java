package io.github.headlesshq.headlessmc.api.command;

import io.github.headlesshq.headlessmc.api.HeadlessMc;

public class PasswordCommand extends AbstractCommand {
    public PasswordCommand(HeadlessMc ctx) {
        super(ctx, "password", "Toggles password mode.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (!ctx.getCommandLine().isHidingPasswordsSupported()) {
            throw new CommandException("Hiding inputs is not supported.");
        }

        ctx.getCommandLine().setHidingPasswords(!ctx.getCommandLine().isHidingPasswords());
        ctx.log((ctx.getCommandLine().isHidingPasswords() ? "En" : "Dis")
                    + "abled password mode.");
    }

}
