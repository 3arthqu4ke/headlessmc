package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.earth.headlessmc.launcher.os.OS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuleTest {
    private static final String rule = "[{\"action\":\"allow\"}," +
        "{\"action\":\"disallow\",\"os\":{\"name\":\"osx\"}}]";

    @Test
    public void testRule() {
        JsonElement element = JsonParser.parseString(rule);
        Rule rule = new RuleFactory().parse(element);
        OS os = new OS("osx", OS.Type.OSX, "1.0.0", false);
        assertEquals(Rule.Action.DISALLOW, rule.apply(os, null));
        OS windows = new OS("windows", OS.Type.WINDOWS, "1.0.0", false);
        assertEquals(Rule.Action.ALLOW, rule.apply(windows, null));
        OS linux = new OS("linux", OS.Type.LINUX, "1.0.0", false);
        assertEquals(Rule.Action.ALLOW, rule.apply(linux, null));
    }

}
