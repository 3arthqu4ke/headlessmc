package me.earth.headlessmc.launcher.version;

import lombok.val;
import me.earth.headlessmc.launcher.UsesResources;
import me.earth.headlessmc.launcher.os.OS;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LibraryFactoryTest implements UsesResources {
    @Test
    public void test() {
        val rf = new RuleFactory();
        val ef = new ExtractorFactory();
        val nf = new NativesFactory();
        val factory = new LibraryFactory(rf, ef, nf);

        val jo = getJsonObject("lib.json");
        val libs = factory.parse(jo);

        assertEquals(1, libs.size());
        OS os = new OS("Windows", OS.Type.WINDOWS, "10", false);
        Features feat = Features.EMPTY;
        assertEquals(Rule.Action.ALLOW, libs.get(0).getRule().apply(os, feat));
        assertEquals("test:test:test", libs.get(0).getName());
        assertEquals("_download_url", libs.get(0).getUrl(""));
        assertFalse(libs.get(0).isNativeLibrary());
    }

}
