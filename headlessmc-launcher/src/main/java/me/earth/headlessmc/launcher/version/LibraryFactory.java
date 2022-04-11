package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.launcher.util.CollectionUtil;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@CustomLog
@RequiredArgsConstructor
class LibraryFactory {
    private final RuleFactory ruleFactory;
    private final ExtractorFactory extractorFactory;
    private final NativesFactory nativesFactory;

    public List<Library> parseLibraries(JsonElement element) {
        if (element == null || !element.isJsonArray()) {
            return Collections.emptyList();
        }

        val result = new ArrayList<Library>(element.getAsJsonArray().size());
        for (val library : element.getAsJsonArray()) {
            if (library.isJsonObject()) {
                result.addAll(parse(library.getAsJsonObject()));
            }
        }

        return result;
    }

    public List<Library> parse(JsonObject json) {
        val rule = ruleFactory.parse(json.get("rules"));
        val extractor = extractorFactory.parse(json.get("extract"));
        val natives = nativesFactory.parse(json.get("natives"));
        val result = new ArrayList<Library>(natives.isEmpty()
                                                ? 1 : natives.size() + 1);
        val name = json.get("name").getAsString();
        val baseUrl = JsonUtil.getString(json, "url");

        val downloads = json.get("downloads");
        if (downloads != null && downloads.isJsonObject()) {
            val artifact = downloads.getAsJsonObject().get("artifact");
            if (artifact != null && artifact.isJsonObject()) {
                val jo = artifact.getAsJsonObject();
                val url = JsonUtil.getString(jo, "url");
                val path = JsonUtil.getString(jo, "path");
                result.add(new LibraryImpl(natives, Extractor.NO_EXTRACTION,
                                           name, rule, baseUrl, url, path,
                                           false));
            }

            val classifiers = downloads.getAsJsonObject().get("classifiers");
            if (classifiers != null && classifiers.isJsonObject()) {
                for (Map.Entry<String, JsonElement> entry :
                    classifiers.getAsJsonObject().entrySet()) {
                    val os = CollectionUtil.getKey(natives, entry.getKey());
                    if (os == null || !entry.getValue().isJsonObject()) {
                        continue;
                    }

                    // stupid but because we went the approach where
                    // we return multiple libraries we need this.
                    Rule osRule = (osIn, f) -> {
                        return os.equalsIgnoreCase(osIn.getType().getName())
                            ? rule.apply(osIn, f) : Rule.Action.DISALLOW;
                    };

                    val jo = entry.getValue().getAsJsonObject();
                    val url = JsonUtil.getString(jo, "url");
                    val path = JsonUtil.getString(jo, "path");
                    result.add(new LibraryImpl(natives, extractor, name, osRule,
                                               baseUrl, url, path, true));
                }
            }
        }

        if (result.isEmpty()) {
            result.add(new LibraryImpl(
                natives, extractor, name, rule, baseUrl, null, null, false));
        }

        return result;
    }

}
