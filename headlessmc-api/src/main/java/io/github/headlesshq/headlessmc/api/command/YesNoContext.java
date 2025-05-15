package io.github.headlesshq.headlessmc.api.command;

import lombok.RequiredArgsConstructor;
import io.github.headlesshq.headlessmc.api.HeadlessMc;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Iterator;

@RequiredArgsConstructor
public class YesNoContext implements CommandContext {
    private final YesNoCallback callback;
    private final HeadlessMc ctx;

    public static void goBackAfter(HeadlessMc ctx, YesNoCallback callback) {
        ctx.getCommandLine().setWaitingForInput(true);
        CommandContext current = ctx.getCommandLine().getCommandContext();
        ctx.getCommandLine().setCommandContext(new YesNoContext(result -> {
            try {
                ctx.getCommandLine().setWaitingForInput(false);
                callback.accept(result);
            } finally {
                ctx.getCommandLine().setCommandContext(current);
            }
        }, ctx));
    }

    @Override
    public void execute(String command) {
        try {
            if ("y".equalsIgnoreCase(command)) {
                callback.accept(true);
            } else if ("n".equalsIgnoreCase(command)) {
                callback.accept(false);
            } else {
                ctx.log("Expected one of y/n...");
            }
        } catch (CommandException ce) {
            ctx.log(ce.getMessage());
        }
    }

    @Override
    public @NotNull Iterator<Command> iterator() {
        return Collections.emptyIterator();
    }

}
