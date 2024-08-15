package me.earth.headlessmc.api.config;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ConfigImplTest {
    @Test
    public void testConfigImpl() {
        val properties = new Properties();
        val expectedName = "test";
        val expectedId = 0;

        val config = new ConfigImpl(properties, expectedName, expectedId);
        Assertions.assertEquals(expectedName, config.getName());
        Assertions.assertEquals(expectedId, config.getId());

        val property = PropertyTypes.string("ConfigImplTest.property");
        assertNull(config.get(property));
        Assertions.assertEquals(expectedName,
                                config.get(property, expectedName));
        Assertions.assertEquals(expectedName,
                                config.getValue(property, () -> expectedName));

        val expectedValue = "test value";
        properties.put(property.getName(), expectedValue);
        Assertions.assertEquals(expectedValue, config.get(property));
        Assertions.assertEquals(expectedValue,
                                config.get(property, expectedName));
        Assertions.assertEquals(expectedValue,
                                config.getValue(property, () -> expectedName));

        val newExpectedValue = "test value 2";
        System.setProperty(property.getName(), newExpectedValue);
        Assertions.assertEquals(newExpectedValue, config.get(property));
        Assertions.assertEquals(newExpectedValue,
                                config.get(property, expectedValue));
        Assertions.assertEquals(newExpectedValue,
                                config.getValue(property, () -> expectedValue));

        Config cfg = ConfigImpl.empty();
        val property2 = PropertyTypes.array("ConfigImplTest.property2", ";");
        System.setProperty("ConfigImplTest.property2", "");
        assertNull(cfg.get(property2));
    }

}
