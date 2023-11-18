package me.earth.headlessmc.launcher.command.login;

import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;

import java.util.function.Consumer;

@CustomLog
public class LoginCommand extends AbstractLauncherCommand {
    public LoginCommand(Launcher ctx) {
        super(ctx, "login", "Logs you into your account.");
        args.put("<email>", "The email of the account to log in to.");
        args.put("<password>", "If you enabled password mode or if you just" +
            " don't care you can enter the password right away.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (CommandUtil.hasFlag("-webview", args)) {
            loginWithWebView();
            return;
        }

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
            ctx.setWaitingForInput(true);
            ctx.setCommandContext(new PasswordContext(ctx, action));
        }
    }

    private Consumer<String> login(String email) {
        val passwordsHiddenBefore = ctx.isHidingPasswords();
        return password -> {
            ctx.log("Logging in...");
            try {
                val account = ctx.getAccountManager().login(email, password);
                ctx.log("Logged into account " + account.getName()
                            + " successfully!");
            } catch (AuthException e) {
                ctx.log("Failed to log you in: " + e.getMessage());
                e.printStackTrace();
            }

            if (!passwordsHiddenBefore) {
                ctx.setHidingPasswords(false);
            }

            ctx.setWaitingForInput(false);
        };
    }

    private void loginWithWebView() {
        ctx.log("Starting web view...");
        try {
            val account = ctx.getAccountManager().loginWithWebView();
            ctx.log("Logged into account " + account.getName()
                        + " successfully.");
        } catch (AuthException e) {
            ctx.log("Failed to log in: " + e.getMessage());
        } catch (NoClassDefFoundError e) {
            log.debug(e.getMessage());
            ctx.log("Your version of Java does not support Webview! It usually comes bundled with JDK 8 or in the headlessmc-launcher-jfx jar.");
        }
    }

}
