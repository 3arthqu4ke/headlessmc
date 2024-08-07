package me.earth.headlessmc.api.command;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.HeadlessMc;

import java.util.Collections;
import java.util.Iterator;

@RequiredArgsConstructor
public class YesNoContext implements CommandContext {
    private final YesNoCallback callback;
    private final HeadlessMc ctx;

    public static void goBackAfter(HeadlessMc ctx, YesNoCallback callback) {
        ctx.setWaitingForInput(true);
        CommandContext current = ctx.getCommandContext();
        ctx.setCommandContext(new YesNoContext(result -> {
            try {
                ctx.setWaitingForInput(false);
                callback.accept(result);
            } finally {
                ctx.setCommandContext(current);
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
    public Iterator<Command> iterator() {
        return Collections.emptyIterator();
    }

}
