package io.github.headlesshq.headlessmc.launcher.version;

import com.google.gson.JsonArray;
import lombok.val;
import io.github.headlesshq.headlessmc.launcher.UsesResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NativesFactoryTest implements UsesResources {
    @Test
    public void testNativesFactory() {
        val element = getJsonObject("lib_natives.json").get("natives");
        val nativesFactory = new NativesFactory();
        Assertions.assertTrue(nativesFactory.parse(null).isEmpty());
        Assertions.assertTrue(nativesFactory.parse(new JsonArray()).isEmpty());
        val natives = nativesFactory.parse(element);
        Assertions.assertEquals(1, natives.size());
        Assertions.assertTrue(natives.containsKey("osx"));
        Assertions.assertEquals("natives-osx", natives.get("osx"));
    }

}
