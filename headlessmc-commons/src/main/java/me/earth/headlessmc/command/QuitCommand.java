package me.earth.headlessmc.command;

import me.earth.headlessmc.api.HeadlessMc;

public class QuitCommand extends AbstractCommand {
    public QuitCommand(HeadlessMc ctx) {
        super(ctx, "quit", "Quits HeadlessMc.");
    }

    @Override
    public void execute(String... args) {
        ctx.log("Bye!");
        System.exit(0);
    }

    @Override
    public boolean matches(String... args) {
        if (args.length > 0) {
            String lower = args[0].toLowerCase().trim();
            return lower.equalsIgnoreCase("quit")
                || lower.equalsIgnoreCase("exit")
                || lower.equalsIgnoreCase("stop");
        }

        return false;
    }

}
