package me.earth.headlessmc.launcher.instrumentation.log4j;

import lombok.experimental.UtilityClass;
import me.earth.headlessmc.launcher.instrumentation.Transformer;

@UtilityClass
public class Patchers {
    public static final Transformer JNDI = new Log4jPatcher(
        "org/apache/logging/log4j/core/lookup/JndiLookup");
    public static final Transformer LOOKUP = new Log4jPatcher(
        "org/apache/logging/log4j/core/lookup/Interpolator");

}
