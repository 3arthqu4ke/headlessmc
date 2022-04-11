package me.earth.headlessmc.config;

import me.earth.headlessmc.api.config.Property;

import static me.earth.headlessmc.config.PropertyTypes.bool;
import static me.earth.headlessmc.config.PropertyTypes.string;

public interface HmcProperties {
    Property<String> MAIN = string("hmc.main_method");
    Property<Boolean> DEENCAPSULATE = bool("hmc.deencapsulate");
    Property<String> LOGLEVEL = string("hmc.loglevel");

}
