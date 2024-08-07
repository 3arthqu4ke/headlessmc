package me.earth.headlessmc.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Properties;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class ConfigImpl implements Config {
    private final Properties properties;
    @Getter
    private final String name;
    @Getter
    private final int id;

    public static Config empty() {
        return new ConfigImpl(new Properties(), "empty", -1);
    }

    @Override
    public <T> T getValue(Property<T> property, Supplier<T> defaultValue) {
        String value = System.getProperty(property.getName());
        if (value == null) {
            value = (String) properties.get(property.getName());
            if (value == null) {
                return defaultValue.get();
            }
        }

        T result = property.parse(value);
        return result == null
            ? defaultValue.get()
            : result;
    }

}
