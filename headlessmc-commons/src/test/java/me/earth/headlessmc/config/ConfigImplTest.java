package me.earth.headlessmc.config;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

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
        Assertions.assertNull(config.get(property));
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
    }

}
