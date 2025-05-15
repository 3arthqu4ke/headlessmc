package io.github.headlesshq.headlessmc.launcher.command.login;

import lombok.CustomLog;
import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.auth.AbstractLoginCommand;
import io.github.headlesshq.headlessmc.launcher.Launcher;
import io.github.headlesshq.headlessmc.launcher.auth.AuthException;
import io.github.headlesshq.headlessmc.auth.ValidatedAccount;
import net.raphimc.minecraftauth.step.java.session.StepFullJavaSession;

@CustomLog
public class LoginCommand extends AbstractLoginCommand {
    private final Launcher launcher;

    public LoginCommand(Launcher ctx) {
        super(ctx);
        this.launcher = ctx;
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        super.execute(line, args);
    }

    @Override
    protected void onSuccessfulLogin(StepFullJavaSession.FullJavaSession session) {
        ValidatedAccount validatedAccount;
        try {
            validatedAccount = launcher.getAccountManager().getAccountValidator().validate(session);
        } catch (AuthException e) {
            ctx.log(e.getMessage());
            return;
        }

        launcher.log("Logged into account " + validatedAccount.getName() + " successfully!");
        launcher.getAccountManager().addAccount(validatedAccount);
    }

}
