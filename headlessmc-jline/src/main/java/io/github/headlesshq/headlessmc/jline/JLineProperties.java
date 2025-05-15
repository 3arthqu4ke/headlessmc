package io.github.headlesshq.headlessmc.jline;

import io.github.headlesshq.headlessmc.api.config.Property;
import io.github.headlesshq.headlessmc.api.config.PropertyTypes;

/**
 * A collection of properties for configuring JLine.
 */
public interface JLineProperties {
    Property<Boolean> ENABLED = PropertyTypes.bool("hmc.jline.enabled");
    Property<Boolean> PROPAGATE_ENABLED = PropertyTypes.bool("hmc.jline.propagate.enabled");

    Property<String> PROVIDERS = PropertyTypes.string("hmc.jline.providers");
    Property<String> READ_PREFIX = PropertyTypes.string("hmc.jline.read.prefix");
    Property<String> TYPE = PropertyTypes.string("hmc.jline.type");

    Property<Boolean> DUMB = PropertyTypes.bool("hmc.jline.dumb");
    Property<Boolean> FORCE_NOT_DUMB = PropertyTypes.bool("hmc.jline.force.not.dumb");
    Property<Boolean> DUMB_WHEN_NO_CONSOLE = PropertyTypes.bool("hmc.jline.dumb.when.no.console");
    Property<Boolean> PREVENT_DEPRECATION_WARNING = PropertyTypes.bool("hmc.jline.no.deprecation.warning");
    Property<Boolean> BRACKETED_PASTE = PropertyTypes.bool("hmc.jline.bracketed.paste");
    Property<Boolean> JLINE_IN = PropertyTypes.bool("hmc.jline.in");
    Property<Boolean> JLINE_OUT = PropertyTypes.bool("hmc.jline.out");
    Property<Boolean> FFM = PropertyTypes.bool("hmc.jline.ffm");
    Property<Boolean> JANSI = PropertyTypes.bool("hmc.jline.jansi");
    Property<Boolean> JNA = PropertyTypes.bool("hmc.jline.jna");
    Property<Boolean> JNI = PropertyTypes.bool("hmc.jline.jni");
    Property<Boolean> EXEC = PropertyTypes.bool("hmc.jline.exec");
    Property<Boolean> SYSTEM = PropertyTypes.bool("hmc.jline.system");

    Property<Boolean> ENABLE_PROGRESS_BAR = PropertyTypes.bool("hmc.jline.enable.progressbar");
    Property<String> PROGRESS_BAR_STYLE = PropertyTypes.string("hmc.jline.progressbar.style");

}
