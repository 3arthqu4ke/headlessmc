package me.earth.headlessmc.launcher.auth;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.launcher.LauncherProperties;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: support Mojang?
// TODO: when another config is loaded invalidate lastAccount?
@CustomLog
@RequiredArgsConstructor
public class AccountManager implements Iterable<Account> {
    private final Map<Integer, Account> cache = new ConcurrentHashMap<>();
    private final AccountStore accountStore;
    private final AccountValidator validator;
    @Getter
    private Account lastAccount;

    public Account login(Config config) throws AuthException {
        log.debug("Attempting to login...");
        val account = accountStore.load();
        if (account.isPresent() && validator.isValid(account.get())) {
            log.debug("Found account " + account + " in account store.");
            lastAccount = account.get();
            return account.get();
        }

        val email = config.get(LauncherProperties.EMAIL);
        val password = config.get(LauncherProperties.PASSWORD);
        if (email != null && password != null) {
            return this.login(email, password);
        }

        log.warning("No valid account found!");
        throw new AuthException("You can't play the game without an account!" +
                                    " Please use the login command.");
    }

    public Account login(String email, String password) throws AuthException {
        val hash = (email + password).hashCode();
        val cachedAccount = cache.get(hash);
        if (cachedAccount != null) {
            return cachedAccount;
        }

        try {
            val authenticator = new MicrosoftAuthenticator();
            val result = authenticator.loginWithCredentials(email, password);
            val account = new Account(result.getProfile().getName(),
                                      result.getProfile().getId(),
                                      result.getAccessToken());
            validator.validate(account);
            cache.put(hash, account);
            lastAccount = account;
            accountStore.save(account);
            return account;
        } catch (MicrosoftAuthenticationException | IOException e) {
            throw new AuthException(e.getMessage());
        }
    }

    @Override
    public Iterator<Account> iterator() {
        return cache.values().iterator();
    }

}
