package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.HeadlessMc;

public class PasswordCommand extends AbstractCommand {
    public PasswordCommand(HeadlessMc ctx) {
        super(ctx, "password", "Toggles password mode.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (!ctx.getCommandLineManager().isHidingPasswordsSupported()) {
            throw new CommandException("Hiding inputs is not supported.");
        }

        ctx.getCommandLineManager().setHidingPasswords(!ctx.getCommandLineManager().isHidingPasswords());
        ctx.log((ctx.getCommandLineManager().isHidingPasswords() ? "En" : "Dis")
                    + "abled password mode.");
    }

}
