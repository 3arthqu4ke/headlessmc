package me.earth.headlessmc.launcher.auth;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import lombok.*;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.launcher.LauncherProperties;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
        var acc = accountStore.load();
        if (acc.isPresent() && (acc = refresh(acc.get())).isPresent()) {
            log.debug("Found account " + acc + " in account store.");
            validator.validate(acc.get());
            lastAccount = acc.get();
            save(acc.get());
            return acc.get();
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
            val account = setAccount(result);
            cache.put(hash, account);
            return account;
        } catch (MicrosoftAuthenticationException e) {
            throw new AuthException(e.getMessage());
        }
    }

    public Account loginWithWebView() throws AuthException {
        val authenticator = new MicrosoftAuthenticator();
        try {
            val result = authenticator.loginWithWebview();
            return setAccount(result);
        } catch (MicrosoftAuthenticationException e) {
            throw new AuthException(e.getMessage());
        }
    }

    public Account setAccount(MicrosoftAuthResult result) throws AuthException {
        val account = toAccount(result);
        validator.validate(account);
        lastAccount = account;
        save(account);
        return account;
    }

    @Override
    public Iterator<Account> iterator() {
        return cache.values().iterator();
    }

    private void save(Account account) {
        try {
            accountStore.save(account);
        } catch (IOException e) {
            log.error("Failed to save account " + account + " : "
                          + e.getMessage());
        }
    }

    private Optional<Account> refresh(Account account) {
        log.debug("Refreshing account " + account);
        val authenticator = new MicrosoftAuthenticator();
        try {
            val result = authenticator.loginWithRefreshToken(
                account.getRefreshToken());
            log.debug("Refreshed account " + account + "successfully");
            return Optional.of(toAccount(result));
        } catch (MicrosoftAuthenticationException e) {
            log.error("Couldn't refresh: " + account + " : " + e.getMessage());
            return Optional.empty();
        }
    }

    private Account toAccount(MicrosoftAuthResult result) {
        return new Account(result.getProfile().getName(),
                           result.getProfile().getId(),
                           result.getAccessToken(),
                           result.getRefreshToken(),
                           result.getXuid(),
                           result.getClientId());
    }

}
