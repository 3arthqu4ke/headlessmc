package me.earth.headlessmc.launcher.version;

import lombok.val;
import me.earth.headlessmc.launcher.UsesResources;
import me.earth.headlessmc.os.OS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuleFactoryTest implements UsesResources {
    @Test
    public void testRule() {
        val element = getJsonElement("rule_os.json");
        val rule = new RuleFactory().parse(element);
        val os = new OS("osx", OS.Type.OSX, "1.0.0", false);
        assertEquals(Rule.Action.DISALLOW, rule.apply(os, null));
        val windows = new OS("windows", OS.Type.WINDOWS, "1.0.0", false);
        assertEquals(Rule.Action.ALLOW, rule.apply(windows, null));
        val linux = new OS("linux", OS.Type.LINUX, "1.0.0", false);
        assertEquals(Rule.Action.ALLOW, rule.apply(linux, null));
    }

    @Test
    public void testRuleWithFeature() {
        val element = getJsonElement("rule_feature.json");
        val rule = new RuleFactory().parse(element);
        Features features = Features.EMPTY;
        val os = new OS("osx", OS.Type.OSX, "1.0.0", false);
        Assertions.assertEquals(Rule.Action.ALLOW, rule.apply(os, features));

        val featureMap = new HashMap<String, Boolean>();
        featureMap.put("feature", true);
        features = new Features(featureMap);
        Assertions.assertEquals(Rule.Action.DISALLOW, rule.apply(os, features));

        featureMap.put("feature", false);
        Assertions.assertEquals(Rule.Action.ALLOW, rule.apply(os, features));
    }

}
