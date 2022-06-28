package me.earth.headlessmc.launcher.version;

import com.google.gson.JsonArray;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.UsesResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExtractorFactoryTest implements UsesResources {
    @Test
    public void testExtractorFactory() {
        val factory = new ExtractorFactory();
        var extractor = factory.parse(null);
        Assertions.assertEquals(Extractor.NO_EXTRACTION, extractor);
        extractor = factory.parse(new JsonArray());
        Assertions.assertEquals(Extractor.NO_EXTRACTION, extractor);
        extractor = factory.parse(getJsonObject("extractor.json"));
        Assertions.assertNotEquals(Extractor.NO_EXTRACTION, extractor);
        Assertions.assertTrue(extractor.isExtracting());
        Assertions.assertTrue(extractor.shouldExtract("test"));
        Assertions.assertFalse(extractor.shouldExtract("META-INF/"));
        Assertions.assertFalse(extractor.shouldExtract("META-INF/test"));
    }

}
