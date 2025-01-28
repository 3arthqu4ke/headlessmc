package me.earth.headlessmc.graalvm;

import me.earth.headlessmc.api.config.Property;

import static me.earth.headlessmc.api.config.PropertyTypes.*;

public interface GraalProperties {
    Property<Long> JAVA_VERSION = number("hmc.graal.java.version");
    Property<String> JAVA_DISTRIBUTION = string("hmc.graal.distribution");
    Property<Boolean> FORCE_DOWNLOAD = bool("hmc.graal.force.download");
    Property<Boolean> JDK = bool("hmc.graal.jdk");

}
