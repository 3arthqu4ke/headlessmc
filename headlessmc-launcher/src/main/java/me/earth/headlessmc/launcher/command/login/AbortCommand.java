package me.earth.headlessmc.launcher.command.login;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.command.AbstractCommand;

public class AbortCommand extends AbstractCommand {
    private final CommandContext context;

    public AbortCommand(HeadlessMc ctx, CommandContext context) {
        super(ctx, "abort", "Aborts entering the password.");
        this.context = context;
    }

    @Override
    public void execute(String... args) {
        ctx.log("Aborting login...");
        ctx.setCommandContext(context);
        ctx.setWaitingForInput(false);
        ctx.setHidingPasswords(false);
    }

}
