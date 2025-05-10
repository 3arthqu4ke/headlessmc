package me.earth.headlessmc.launcher.auth;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.auth.AccountJsonLoader;
import me.earth.headlessmc.auth.ValidatedAccount;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.files.LauncherConfig;

import java.io.File;
import java.io.IOException;
import java.util.List;

@CustomLog
@RequiredArgsConstructor
public class AccountStore {
    private final AccountJsonLoader accountJsonLoader;
    private final LauncherConfig launcherConfig;

    public AccountStore(LauncherConfig launcherConfig) {
        this(new AccountJsonLoader(), launcherConfig);
    }

    public void save(List<ValidatedAccount> accounts) throws IOException {
        if (!launcherConfig.getConfig().getConfig().get(LauncherProperties.STORE_ACCOUNTS, true)) {
            return;
        }

        File file = launcherConfig.getFileManager().create("auth", ".accounts.json");
        accountJsonLoader.save(file.toPath(), accounts);
    }

    public List<ValidatedAccount> load() throws IOException {
        File file = launcherConfig.getFileManager().create("auth", ".accounts.json");
        return accountJsonLoader.load(file.toPath());
    }

}
