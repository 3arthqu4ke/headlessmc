package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonElement;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.launcher.LauncherProperties;

@RequiredArgsConstructor
class JavaMajorVersionParser {
    private final Config config;

    public int parse(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return config.get(LauncherProperties.DEFAULT_JAVA, 8L).intValue();
        }

        val version = element.getAsJsonObject().get("majorVersion");
        return version != null
            ? Integer.parseInt(version.getAsString())
            : config.get(LauncherProperties.DEFAULT_JAVA, 8L).intValue();
    }

}
