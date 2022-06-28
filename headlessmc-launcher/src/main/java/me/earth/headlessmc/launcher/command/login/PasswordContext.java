package me.earth.headlessmc.launcher.command.login;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandContextImpl;
import me.earth.headlessmc.command.HelpCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PasswordContext extends CommandContextImpl {
    private final PasswordCommand passwordCommand;

    public PasswordContext(HeadlessMc ctx, Consumer<String> action) {
        this(ctx, action, ctx.getCommandContext());
    }

    public PasswordContext(HeadlessMc ctx,
                           Consumer<String> action,
                           CommandContext commandContext) {
        super(ctx);
        passwordCommand = new PasswordCommand(ctx, commandContext, action);
        add(passwordCommand);
        add(new AbortCommand(ctx, commandContext));
        add(new HelpCommand(ctx));
    }

    @Override
    protected void fail(String... args) {
        List<String> arguments = new ArrayList<>(args.length + 1);
        arguments.add("password");
        arguments.addAll(Arrays.asList(args));
        try {
            passwordCommand.execute(arguments.toArray(new String[0]));
        } catch (CommandException commandException) {
            log.log(commandException.getMessage());
        }
    }

}
