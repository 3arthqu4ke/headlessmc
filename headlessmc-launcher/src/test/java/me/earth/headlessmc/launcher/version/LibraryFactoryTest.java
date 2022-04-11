package me.earth.headlessmc.launcher.version;

import lombok.Cleanup;
import lombok.val;
import me.earth.headlessmc.launcher.os.OS;
import me.earth.headlessmc.launcher.util.JsonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibraryFactoryTest {
    @Test
    public void test() throws IOException {
        val rf = new RuleFactory();
        val ef = new ExtractorFactory();
        val nf = new NativesFactory();
        val factory = new LibraryFactory(rf, ef, nf);

        @Cleanup
        val is = getClass().getClassLoader().getResourceAsStream("lib1.json");
        val je = JsonUtil.fromInput(is);

        val libs = factory.parse(je.getAsJsonObject());

        assertEquals(1, libs.size());
        OS os = new OS("Windows", OS.Type.WINDOWS, "10", false);
        Features feat = Features.EMPTY;
        assertEquals(Rule.Action.ALLOW, libs.get(0).getRule().apply(os, feat));
    }

}
