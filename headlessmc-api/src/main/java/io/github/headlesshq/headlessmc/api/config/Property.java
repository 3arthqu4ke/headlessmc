package io.github.headlesshq.headlessmc.api.config;

import io.github.headlesshq.headlessmc.api.HasName;

public interface Property<T> extends HasName {
    T parse(String value);

}
