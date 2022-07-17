package me.earth.headlessmc.launcher.auth;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import lombok.Cleanup;
import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.launcher.util.URLs;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Validates that an account actually owns the game.
 */
@CustomLog
public class AccountValidator {
    private static final URL URL = URLs.url(
        "https://api.minecraftservices.com/entitlements/mcstore");

    public boolean isValid(Account account) {
        try {
            validate(account);
            return true;
        } catch (AuthException e) {
            log.error("Failed to validate account " + account);
            return false;
        }
    }

    public void validate(Account account) throws AuthException {
        log.debug("Validating account " + account);
        try {
            val con = (HttpURLConnection) URL.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization",
                                   "Bearer " + account.getToken());
            con.setConnectTimeout(60_000);
            con.setReadTimeout(60_000);

            int status = con.getResponseCode();
            // NonNullReader to prevent NPE? Not sure if that happens
            @SuppressWarnings("UnusedAssignment") @Cleanup
            Reader streamReader = NonNullReader.INSTANCE;
            if (status > 299) {
                streamReader = new InputStreamReader(con.getErrorStream());
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            val je = JsonParser.parseReader(streamReader);
            check(je != null && je.isJsonObject(),
                  "Couldn't read response: " + je);

            assert je != null;
            val items = je.getAsJsonObject().get("items");
            check(items != null && items.isJsonArray(),
                  "Couldn't read items: " + items);

            assert items != null;
            val itemArray = items.getAsJsonArray();
            check(itemArray.size() > 0, "You don't own the game!");
            log.debug(itemArray.toString());
        } catch (IOException | JsonParseException e) {
            log.error("Failed to validate " + account + " : " + e.getMessage());
            throw new AuthException(e.getMessage());
        }
    }

    private void check(boolean condition, String message) throws AuthException {
        if (!condition) {
            log.error(message);
            throw new AuthException(message);
        }
    }

    private static final class NonNullReader extends Reader {
        public static final NonNullReader INSTANCE = new NonNullReader();

        @Override
        @SuppressWarnings("NullableProblems")
        public int read(char[] cbuf, int off, int len) {
            return -1;
        }

        @Override
        public void close() {

        }
    }

}
