package io.github.headlesshq.headlessmc.graalvm;

import io.github.headlesshq.headlessmc.api.config.Property;

public interface GraalProperties {
    Property<Long> JAVA_VERSION = number("hmc.graal.java.version");
    Property<String> JAVA_DISTRIBUTION = string("hmc.graal.distribution");
    Property<Boolean> FORCE_DOWNLOAD = bool("hmc.graal.force.download");
    Property<Boolean> JDK = bool("hmc.graal.jdk");

}
