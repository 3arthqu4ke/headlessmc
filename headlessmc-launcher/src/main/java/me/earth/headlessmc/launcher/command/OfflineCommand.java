package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;

public class OfflineCommand extends AbstractLauncherCommand {
    public OfflineCommand(Launcher ctx) {
        super(ctx, "offline", "Toggles Offline mode.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        boolean value = !ctx.getAccountManager().getOfflineChecker().isOffline();
        if (args.length > 1) {
            value = Boolean.parseBoolean(args[1]);
        }

        ctx.getAccountManager().getOfflineChecker().setOffline(value);
        ctx.log("You are now " + (value ? "offline." : "online"));
    }

}
