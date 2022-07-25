package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class RuleFactory {
    public Rule parse(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonArray()) {
            return Rule.ALLOW;
        }

        val jsonArray = jsonElement.getAsJsonArray();
        val rules = new ArrayList<Rule>(jsonArray.size() + 1);
        rules.add(Rule.DISALLOW); // libraries seem to be disallowed by default
        for (val ruleElement : jsonArray) {
            if (!ruleElement.isJsonObject()) {
                continue;
            }

            val ruleJo = ruleElement.getAsJsonObject();
            rules.add(parse(ruleJo));
        }

        return ofRules(rules);
    }

    private Rule parse(JsonObject ruleJo) {
        val a = ruleJo.get("action");
        if (a == null) {
            return Rule.UNDECIDED;
        }

        val action = Rule.Action.valueOf(a.getAsString().toUpperCase());
        val os = ruleJo.get("os");
        val feat = ruleJo.get("features");
        val rules = new ArrayList<Rule>(os != null && feat != null ? 3 : 2);
        if (os != null) {
            rules.add(parseOs(os.getAsJsonObject(), action));
        }

        if (feat != null) {
            val m = JsonUtil.toBoolMap(feat.getAsJsonObject());
            rules.add(ofFeature(m, action));
        }

        if (rules.isEmpty()) {
            rules.add((operatingSystem, feature) -> action);
        }

        return ofRules(rules);
    }

    private Rule parseOs(JsonObject os, Rule.Action action) {
        val osType = os.getAsJsonObject().get("name");
        val type = osType == null ? null : osType.getAsString();

        Pattern version = null;
        val versionObject = os.getAsJsonObject().get("version");
        if (versionObject != null) {
            version = Pattern.compile(versionObject.getAsString());
        }

        // val arch = os.getAsJsonObject().get("arch"); TODO: this?
        return ofOs(type, version, action);
    }

    private Rule ofOs(String type, Pattern version, Rule.Action action) {
        val osType = type == null ? null : OS.Type.valueOf(type.toUpperCase());
        return (os, features) -> osType != null && osType != os.getType()
            || version != null && !version.matcher(os.getVersion()).find()
            ? Rule.Action.UNDECIDED
            : action;
    }

    private Rule ofFeature(Map<String, Boolean> features, Rule.Action action) {
        return (os, f) ->
            features.entrySet()
                    .stream()
                    .allMatch(e -> f.getFeature(e.getKey()) == e.getValue())
                ? action : Rule.Action.UNDECIDED;
    }

    private Rule ofRules(List<Rule> rules) {
        return (os, features) -> {
            var result = Rule.Action.UNDECIDED;
            for (val rule : rules) {
                val action = rule.apply(os, features);
                result = action == Rule.Action.UNDECIDED ? result : action;
            }

            return result;
        };
    }

}
