package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
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
                for (Map.Entry<String, JsonElement> e :
                    classifiers.getAsJsonObject().entrySet()) {
                    val nativeEntry = getNativeEntry(natives, e.getKey());
                    if (nativeEntry == null || !e.getValue().isJsonObject()) {
                        continue;
                    }

                    // nativeWithReplace for this library, ${arch} is replaced
                    val nativeName = e.getKey();
                    // name of the os
                    val os = nativeEntry.getKey();
                    // native name containing ${arch} to be replaced with 32/64
                    val nativeWithReplace = nativeEntry.getValue();
                    Rule osRule = (osIn, f) -> {
                        // check that we have the right os
                        if (os.equalsIgnoreCase(osIn.getType().getName())
                            // check that we have the right arch version
                            && nativeWithReplace.replace(
                                "${arch}", osIn.isArch() ? "64" : "32")
                                                .equals(nativeName)) {
                            return rule.apply(osIn, f);
                        }

                        return Rule.Action.DISALLOW;
                    };

                    var nativeExtractor = extractor;
                    // if there was no extraction rule specified.
                    if (!extractor.isExtracting()) {
                        nativeExtractor = new ExtractorImpl();
                    }

                    val jo = e.getValue().getAsJsonObject();
                    val url = JsonUtil.getString(jo, "url");
                    val path = JsonUtil.getString(jo, "path");
                    result.add(new LibraryImpl(natives, nativeExtractor, name,
                                               osRule, baseUrl, url, path,
                                               true));
                }
            }
        }

        if (result.isEmpty()) {
            result.add(new LibraryImpl(
                natives, extractor, name, rule, baseUrl, null, null, false));
        }

        return result;
    }

    private Map.Entry<String, String> getNativeEntry(Map<String, String> map,
                                                     String classifier) {
        for (Map.Entry<String, String> e : map.entrySet()) {
            if (e.getValue().replace("${arch}", "32").equals(classifier)
                || e.getValue().replace("${arch}", "64").equals(classifier)) {
                return e;
            }
        }

        return null;
    }

}
