package me.earth.headlessmc.launcher.command.login;

import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;

import java.util.function.Consumer;

@CustomLog
public class LoginCommand extends AbstractLauncherCommand {
    public LoginCommand(Launcher ctx) {
        super(ctx, "login", "Logs you into your account.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 2) {
            throw new CommandException("Please specify an email!");
        }

        val action = login(args[1]);
        if (args.length > 2) {
            if (args.length > 3) {
                log.warning(
                    "Found more than one arg, if you want to use a password"
                        + " containing spaces please escape it with \"");
            }

            action.accept(args[2]);
        } else {
            ctx.log("Please use 'password <password>' to login." +
                        " Use 'abort' to cancel the login process.");
            ctx.setHidingPasswords(true);
            ctx.setCommandContext(new PasswordContext(ctx, action));
        }
    }

    private Consumer<String> login(String email) {
        val passwordsHiddenBefore = ctx.isHidingPasswords();
        return password -> {
            try {
                ctx.getAccountManager().login(email, password);
            } catch (AuthException e) {
                ctx.log("Failed to log you in: " + e.getMessage());
                e.printStackTrace();
            }

            if (!passwordsHiddenBefore) {
                ctx.setHidingPasswords(false);
            }
        };
    }

}
