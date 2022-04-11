package me.earth.headlessmc.command;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandException;

public class PasswordCommand extends AbstractCommand {
    public PasswordCommand(HeadlessMc ctx) {
        super(ctx, "password", "Toggles password mode.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (!ctx.isHidingPasswordsSupported()) {
            throw new CommandException("Hiding inputs is not supported.");
        }

        ctx.setHidingPasswords(!ctx.isHidingPasswords());
        ctx.log((ctx.isHidingPasswords() ? "En" : "Dis")
                    + "abled password mode.");
    }

}
