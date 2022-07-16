package me.earth.headlessmc.launcher.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

// TODO: instead use the launcher_profiles_microsoft_store.json or
//  launcher_accounts.json, but in order to do that we need to figure out
//  all the fields in that file.
@CustomLog
@RequiredArgsConstructor
public class AccountStore {
    private final FileManager fileManager;
    private final HasConfig cfg;

    public Optional<Account> load() {
        log.debug("Loading .accounts.json...");
        val jo = loadFile();
        Account account = null;
        if (!jo.entrySet().isEmpty()) { // file has just been created
            try {
                account = JsonUtil.GSON.fromJson(jo, Account.class);
                log.debug("Found account in .accounts.json");
            } catch (JsonSyntaxException e) {
                log.error("Couldn't read .accounts: " + e.getMessage());
            }
        } else {
            log.debug(".accounts.json is empty");
        }

        return Optional.ofNullable(account);
    }

    public void save(Account account) throws IOException {
        if (!cfg.getConfig().get(LauncherProperties.STORE_ACCOUNTS, false)) {
            return;
        }

        log.debug("Storing " + account);
        val obj = JsonUtil.PRETTY_PRINT.toJsonTree(account);
        val file = fileManager.create("auth", ".account.json");
        try (val fw = new FileWriter(file)) {
            JsonUtil.PRETTY_PRINT.toJson(obj, fw);
        } catch (JsonIOException e) {
            throw new IOException(e);
        }
    }

    private JsonObject loadFile() {
        val file = fileManager.create("auth", ".account.json");
        JsonElement je = null;
        try {
            je = JsonUtil.fromFile(file);
        } catch (IOException e) {
            log.error(file.getName() + ": " + e.getMessage());
        }

        return je == null || !je.isJsonObject()
            ? new JsonObject()
            : je.getAsJsonObject();
    }

}
