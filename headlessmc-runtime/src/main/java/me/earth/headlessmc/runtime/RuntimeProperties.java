package me.earth.headlessmc.runtime;

import me.earth.headlessmc.api.config.Property;
import me.earth.headlessmc.config.HmcProperties;

import static me.earth.headlessmc.config.PropertyTypes.number;

public interface RuntimeProperties extends HmcProperties {
    Property<Long> VM_SIZE = number("hmc.vm_size");

}
