package io.github.headlesshq.headlessmc.runtime;

import io.github.headlesshq.headlessmc.api.config.HmcProperties;
import io.github.headlesshq.headlessmc.api.config.Property;

import static io.github.headlesshq.headlessmc.api.config.PropertyTypes.bool;
import static io.github.headlesshq.headlessmc.api.config.PropertyTypes.number;

public interface RuntimeProperties extends HmcProperties {
    Property<Long> VM_SIZE = number("hmc.vm_size");
    Property<Boolean> ENABLE_REFLECTION = bool("hmc.enable.reflection");
    Property<Boolean> DONT_ASK_FOR_QUIT = bool("hmc.dont.ask.for.quit");

}
