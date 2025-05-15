package io.github.headlesshq.headlessmc.launcher.version;

import com.google.gson.JsonElement;
import io.github.headlesshq.headlessmc.launcher.util.JsonUtil;

import java.util.Collections;
import java.util.Map;

class NativesFactory {
    public Map<String, String> parse(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return Collections.emptyMap();
        }

        return JsonUtil.toStringMap(element.getAsJsonObject());
    }

}
