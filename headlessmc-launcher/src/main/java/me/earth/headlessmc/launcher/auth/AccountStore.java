package me.earth.headlessmc.launcher.auth;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.config.HasConfig;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@CustomLog
@RequiredArgsConstructor
public class AccountStore {
    private final FileManager fileManager;
    private final HasConfig cfg;

    public void save(List<ValidatedAccount> accounts) throws IOException {
        if (!cfg.getConfig().get(LauncherProperties.STORE_ACCOUNTS, true)) {
            return;
        }

        JsonArray array = new JsonArray();
        for (ValidatedAccount account : accounts) {
            try {
                JsonObject object = account.toJson();
                array.add(object);
            } catch (Exception e) {
                log.error("Failed to serialize JavaSession " + account + ": " + e.getMessage(), e);
            }
        }

        JsonObject object = new JsonObject();
        object.add("accounts", array);
        File file = fileManager.create("auth", ".accounts.json");
        String string = JsonUtil.PRETTY_PRINT.toJson(object);
        try (OutputStream os = Files.newOutputStream(file.toPath())) {
            os.write(string.getBytes(StandardCharsets.UTF_8));
        }
    }

    public List<ValidatedAccount> load() throws IOException {
        File file = fileManager.create("auth", ".accounts.json");
        JsonElement je = JsonUtil.fromFile(file);

        JsonArray array = JsonUtil.getArray(je, "accounts");
        List<ValidatedAccount> accounts = new ArrayList<>();
        if (array == null) {
            return accounts;
        }

        for (JsonElement element : array) {
            if (element instanceof JsonObject) {
                try {
                    ValidatedAccount loadedSession = ValidatedAccount.fromJson(element.getAsJsonObject());
                    accounts.add(loadedSession);
                } catch (Exception e) {
                    log.error("Couldn't read account in .accounts.json " + e.getMessage(), e);
                }
            }
        }

        return accounts;
    }

}
