package io.github.headlesshq.headlessmc.os;

import io.github.headlesshq.headlessmc.api.config.Property;

import static io.github.headlesshq.headlessmc.api.config.PropertyTypes.bool;
import static io.github.headlesshq.headlessmc.api.config.PropertyTypes.string;

public interface OsProperties {
    Property<String> OS_NAME = string("hmc.osname");
    Property<String> OS_TYPE = string("hmc.ostype");
    Property<String> OS_VERSION = string("hmc.osversion");
    Property<Boolean> OS_ARCH = bool("hmc.osarch");
    Property<String> OS_ARCHITECTURE = string("hmc.osarchitecture");

}
