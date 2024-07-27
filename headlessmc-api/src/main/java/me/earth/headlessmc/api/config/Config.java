package me.earth.headlessmc.api.config;

import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;

import java.util.function.Supplier;

// TODO: to make the use of configs easier
//  we could allow Configs to inherit other configs
public interface Config extends HasName, HasId {
    <T> T getValue(Property<T> property, Supplier<T> defaultValue);

    default <T> T get(Property<T> property) {
        return getValue(property, () -> null);
    }

    default <T> T get(Property<T> property, T defaultValue) {
        return getValue(property, () -> defaultValue);
    }

}
