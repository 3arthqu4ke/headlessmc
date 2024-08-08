package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.HeadlessMc;

public class PasswordCommand extends AbstractCommand {
    public PasswordCommand(HeadlessMc ctx) {
        super(ctx, "password", "Toggles password mode.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (!ctx.getCommandLine().isHidingPasswordsSupported()) {
            throw new CommandException("Hiding inputs is not supported.");
        }

        ctx.getCommandLine().setHidingPasswords(!ctx.getCommandLine().isHidingPasswords());
        ctx.log((ctx.getCommandLine().isHidingPasswords() ? "En" : "Dis")
                    + "abled password mode.");
    }

}
