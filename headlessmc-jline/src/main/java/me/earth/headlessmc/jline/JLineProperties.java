package me.earth.headlessmc.jline;

import me.earth.headlessmc.api.config.Property;
import me.earth.headlessmc.api.config.PropertyTypes;

public interface JLineProperties {
    Property<Boolean> ENABLED = PropertyTypes.bool("hmc.jline.enabled");
    Property<String> PROVIDERS = PropertyTypes.string("hmc.jline.providers");
    Property<String> READ_PREFIX = PropertyTypes.string("hmc.jline.read.prefix");
    Property<Boolean> DUMB = PropertyTypes.bool("hmc.jline.dumb");
    Property<Boolean> PREVENT_DEPRECATION_WARNING = PropertyTypes.bool("hmc.jline.no.deprecation.warning");
    Property<Boolean> BRACKETED_PASTE = PropertyTypes.bool("hmc.jline.bracketed.paste");
    Property<Boolean> JLINE_IN = PropertyTypes.bool("hmc.jline.in");
    Property<Boolean> JLINE_OUT = PropertyTypes.bool("hmc.jline.out");

    Property<Boolean> FFM = PropertyTypes.bool("hmc.jline.ffm");
    Property<Boolean> JANSI = PropertyTypes.bool("hmc.jline.jansi");
    Property<Boolean> JNA = PropertyTypes.bool("hmc.jline.jna");

}
