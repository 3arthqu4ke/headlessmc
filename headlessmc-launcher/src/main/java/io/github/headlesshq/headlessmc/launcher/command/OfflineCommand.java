package io.github.headlesshq.headlessmc.launcher.command;

import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.launcher.Launcher;

public class OfflineCommand extends AbstractLauncherCommand {
    public OfflineCommand(Launcher ctx) {
        super(ctx, "offline", "Toggles Offline mode.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        boolean value = !ctx.getAccountManager().getOfflineChecker().isOffline();
        if (args.length > 1) {
            value = Boolean.parseBoolean(args[1]);
        }

        ctx.getAccountManager().getOfflineChecker().setOffline(value);
        ctx.log("You are now " + (value ? "offline." : "online."));
    }

}
