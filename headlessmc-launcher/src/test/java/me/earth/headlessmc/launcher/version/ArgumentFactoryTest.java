package me.earth.headlessmc.launcher.version;

import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.UsesResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArgumentFactoryTest implements UsesResources {
    @Test
    @SneakyThrows
    public void testParseArguments() {
        val factory = new ArgumentFactory(new RuleFactory());
        val format = new boolean[]{false};
        Assertions.assertNull(factory.parse(null, f -> format[0] = f));
        Assertions.assertFalse(format[0]);

        var args = factory.parse(getJsonElement("arguments_old.json"),
                                 f -> format[0] = f);
        Assertions.assertFalse(format[0]);
        Assertions.assertEquals(4, args.size());
        args.forEach(arg -> {
            Assertions.assertEquals("game", arg.getType());
            Assertions.assertEquals(Rule.ALLOW, arg.getRule());
        });
        Assertions.assertEquals("--userType", args.get(0).getValue());
        Assertions.assertEquals("${user_type}", args.get(1).getValue());
        Assertions.assertEquals("--versionType", args.get(2).getValue());
        Assertions.assertEquals("${version_type}", args.get(3).getValue());

        args = factory.parse(getJsonObject("arguments.json"),
                             f -> format[0] = f);
        Assertions.assertTrue(format[0]);
        Assertions.assertEquals(7, args.size());

        Assertions.assertEquals("game", args.get(0).getType());
        Assertions.assertEquals("game", args.get(1).getType());
        Assertions.assertEquals("game", args.get(2).getType());
        Assertions.assertEquals("game", args.get(3).getType());
        Assertions.assertEquals("game", args.get(4).getType());

        Assertions.assertEquals("jvm", args.get(5).getType());
        Assertions.assertEquals("jvm", args.get(6).getType());

        Assertions.assertEquals(Rule.ALLOW, args.get(0).getRule());
        Assertions.assertEquals(Rule.ALLOW, args.get(1).getRule());
        Assertions.assertEquals(Rule.ALLOW, args.get(5).getRule());
        Assertions.assertEquals(Rule.ALLOW, args.get(6).getRule());

        Assertions.assertNotEquals(Rule.ALLOW, args.get(2).getRule());
        Assertions.assertNotEquals(Rule.ALLOW, args.get(3).getRule());
        Assertions.assertNotEquals(Rule.ALLOW, args.get(4).getRule());

        Assertions.assertEquals("--arg", args.get(0).getValue());
        Assertions.assertEquals("${arg}", args.get(1).getValue());
        Assertions.assertEquals("--demo", args.get(2).getValue());
        Assertions.assertEquals("value1", args.get(3).getValue());
        Assertions.assertEquals("value2", args.get(4).getValue());
        Assertions.assertEquals("--something", args.get(5).getValue());
        Assertions.assertEquals("${something}", args.get(6).getValue());
    }

}
