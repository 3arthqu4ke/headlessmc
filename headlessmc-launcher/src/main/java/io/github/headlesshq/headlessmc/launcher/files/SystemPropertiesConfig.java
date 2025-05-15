package io.github.headlesshq.headlessmc.launcher.files;

import lombok.val;
import io.github.headlesshq.headlessmc.api.config.Property;

import java.util.function.Supplier;

public class SystemPropertiesConfig extends AbstractConfig {
    public SystemPropertiesConfig(String name, int id) {
        super(name, id);
    }

    @Override
    public <T> T getValue(Property<T> property, Supplier<T> defaultValue) {
        val result = property.parse(System.getProperty(property.getName()));
        return result == null ? defaultValue.get() : result;
    }

}
