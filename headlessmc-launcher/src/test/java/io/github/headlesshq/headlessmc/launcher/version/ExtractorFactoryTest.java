package io.github.headlesshq.headlessmc.launcher.version;

import com.google.gson.JsonArray;
import lombok.val;
import io.github.headlesshq.headlessmc.launcher.UsesResources;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExtractorFactoryTest implements UsesResources {
    @Test
    public void testExtractorFactory() {
        val factory = new ExtractorFactory();
        Extractor extractor = factory.parse(null);
        assertEquals(Extractor.NO_EXTRACTION, extractor);
        extractor = factory.parse(new JsonArray());
        assertEquals(Extractor.NO_EXTRACTION, extractor);
        extractor = factory.parse(getJsonObject("extractor.json"));
        assertNotEquals(Extractor.NO_EXTRACTION, extractor);

        assertTrue(extractor.isExtracting());
        assertTrue(extractor.shouldExtract("test"));
        assertFalse(extractor.shouldExtract("META-INF/"));
        assertFalse(extractor.shouldExtract("META-INF/test"));
    }

    @Test
    public void testExtractorNoExtraction() {
        assertFalse(Extractor.NO_EXTRACTION.isExtracting());
        assertFalse(Extractor.NO_EXTRACTION.shouldExtract("test"));
        val extractor = new ExtractorImpl();
        assertTrue(extractor.isExtracting());
        assertTrue(extractor.shouldExtract("test"));
    }

}
