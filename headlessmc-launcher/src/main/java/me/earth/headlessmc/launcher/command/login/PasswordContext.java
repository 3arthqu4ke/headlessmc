package me.earth.headlessmc.launcher.command.login;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.command.CommandContextImpl;
import me.earth.headlessmc.command.HelpCommand;

import java.util.function.Consumer;

public class PasswordContext extends CommandContextImpl {
    public PasswordContext(HeadlessMc ctx, Consumer<String> action) {
        this(ctx, action, ctx.getCommandContext());
    }

    public PasswordContext(HeadlessMc ctx,
                           Consumer<String> action,
                           CommandContext commandContext) {
        super(ctx);
        add(new PasswordCommand(ctx, commandContext, action));
        add(new AbortCommand(ctx, commandContext));
        add(new HelpCommand(ctx));
    }

}
