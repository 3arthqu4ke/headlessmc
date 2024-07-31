package me.earth.headlessmc.launcher.command;

import lombok.CustomLog;
import lombok.Data;
import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.auth.ValidatedAccount;
import me.earth.headlessmc.util.Table;

import java.util.ArrayList;
import java.util.List;

@CustomLog
public class AccountsCommand extends AbstractLauncherCommand implements FindByCommand<AccountsCommand.ValidatedAccountWithId> {
    public AccountsCommand(Launcher ctx) {
        super(ctx, "account", "List accounts or chose the primary account.");
    }

    @Override
    public void execute(String... args) throws CommandException {
        if (args.length < 2) {
            logTable();
            return;
        }

        FindByCommand.super.execute(args);
    }

    @Override
    public void execute(ValidatedAccountWithId account, String... args) throws CommandException {
        ctx.getAccountManager().addAccount(account.getAccount()); // this will make this account the primary account
        logTable();
        ctx.log("");
        ctx.log("Account " + account.getName() + " selected.");
    }

    @Override
    public Iterable<ValidatedAccountWithId> getIterable() {
        List<ValidatedAccountWithId> result = new ArrayList<>();
        int id = 0;
        for (ValidatedAccount account : ctx.getAccountManager().getAccounts()) {
            result.add(new ValidatedAccountWithId(account, id++));
        }

        return result;
    }

    @Data
    public static class ValidatedAccountWithId implements HasId, HasName {
        private final ValidatedAccount account;
        private final int id;

        @Override
        public String getName() {
            return account.getName();
        }
    }

    private void logTable() {
        ctx.log(new Table<ValidatedAccountWithId>()
                    .withColumn("id", v -> String.valueOf(v.getId()))
                    .withColumn("name", HasName::getName)
                    .addAll(getIterable())
                    .build());
    }

}
