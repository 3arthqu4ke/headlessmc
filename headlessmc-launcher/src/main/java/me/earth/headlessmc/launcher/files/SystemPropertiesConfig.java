package me.earth.headlessmc.launcher.files;

import lombok.val;
import me.earth.headlessmc.api.config.Property;

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

    @Override
    public <T> T setValue(Property<T> property, Supplier<T> value) {
        return property.parse(System.setProperty(property.getName(), value.get().toString()));
    }
}
