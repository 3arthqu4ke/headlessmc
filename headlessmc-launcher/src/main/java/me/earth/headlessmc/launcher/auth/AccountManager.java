package me.earth.headlessmc.launcher.auth;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import lombok.CustomLog;
import lombok.Getter;
import lombok.val;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.launcher.LauncherProperties;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: support Mojang?
// TODO: when another config is loaded invalidate lastAccount?
@CustomLog
public class AccountManager implements Iterable<Account> {
    private final Map<Integer, Account> cache = new ConcurrentHashMap<>();
    @Getter
    private Account lastAccount;

    public Account login(Config config) throws AuthException {
        val email = config.get(LauncherProperties.EMAIL);
        val password = config.get(LauncherProperties.PASSWORD);
        if (email != null && password != null) {
            return this.login(email, password);
        }

        log.warning("No Account has been specified!");
        // TODO: check what else needs to be done to make this comply to any
        //  EULA etc., the auth lib already checks for possession of the game
        throw new AuthException("You can't play the game without an account!");
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
            val account = new Account(result.getAccessToken(),
                                      result.getProfile().getName(),
                                      result.getProfile().getId());
            cache.put(hash, account);
            lastAccount = account;
            return account;
        } catch (MicrosoftAuthenticationException e) {
            throw new AuthException();
        }
    }

    @Override
    public Iterator<Account> iterator() {
        return cache.values().iterator();
    }

}
