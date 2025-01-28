package me.earth.headlessmc.os;

import me.earth.headlessmc.api.config.Property;

import static me.earth.headlessmc.api.config.PropertyTypes.bool;
import static me.earth.headlessmc.api.config.PropertyTypes.string;

public interface OsProperties {
    Property<String> OS_NAME = string("hmc.osname");
    Property<String> OS_TYPE = string("hmc.ostype");
    Property<String> OS_VERSION = string("hmc.osversion");
    Property<Boolean> OS_ARCH = bool("hmc.osarch");
    Property<String> OS_ARCHITECTURE = string("hmc.osarchitecture");

}
