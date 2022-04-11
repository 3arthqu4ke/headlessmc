package me.earth.headlessmc.api.config;

import me.earth.headlessmc.api.HasName;

public interface Property<T> extends HasName {
    T parse(String value);

}
