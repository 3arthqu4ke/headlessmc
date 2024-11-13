package me.earth.headlessmc.launcher.instrumentation;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.launcher.instrumentation.log4j.Patchers;
import me.earth.headlessmc.launcher.instrumentation.lwjgl.HmcLwjglTransformer;
import me.earth.headlessmc.launcher.instrumentation.modlauncher.BootstrapLauncherTransformer;
import me.earth.headlessmc.launcher.instrumentation.paulscode.PaulscodeTransformer;
import me.earth.headlessmc.launcher.instrumentation.xvfb.XvfbLwjglTransformer;
import me.earth.headlessmc.launcher.launch.LaunchOptions;
import me.earth.headlessmc.launcher.version.Library;
import me.earth.headlessmc.launcher.version.family.FamilyUtil;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

@CustomLog
@UtilityClass
public class InstrumentationHelper {
    public static final String RUNTIME_JAR = "headlessmc-runtime.jar";
    public static final String LWJGL_JAR = "headlessmc-lwjgl.jar";

    public static Instrumentation create(LaunchOptions options) {
        val transformers = new ArrayList<Transformer>(8);
        if (options.isLwjgl()) {
            transformers.add(new HmcLwjglTransformer());
            transformers.add(new ResourceExtractor(options.getFiles(), LWJGL_JAR));
        }

        if (options.isPaulscode()) {
            transformers.add(new PaulscodeTransformer());
        }

        if (options.isJndi()) {
            transformers.add(Patchers.JNDI);
        }

        if (options.isRuntime()) {
            transformers.add(new ResourceExtractor(options.getFiles(), RUNTIME_JAR));
        }

        if (options.isLookup()) {
            transformers.add(Patchers.LOOKUP);
        }

        if (options.isInMemory()) {
            transformers.add(new BootstrapLauncherTransformer());
        }

        if (options.isXvfb()) {
            addXvfbTransformer(options, transformers);
        }

        return new Instrumentation(transformers, options.getFiles().getBase());
    }

    @VisibleForTesting
    static void addXvfbTransformer(LaunchOptions options, List<Transformer> transformers) {
        log.error("Hello?!");
        Boolean oldLwjgl = FamilyUtil.iterateParents(options.getVersion(), version -> {
            for (Library library : version.getLibraries()) {
                log.error("Cehcking " + library.getName());
                if ("org.lwjgl.lwjgl".equals(library.getPackage()) && library.getVersionNumber().startsWith("2")) {
                    log.error("Hello? " + library);
                    return true;
                }
            }

            return null;
        });

        if (oldLwjgl != null && oldLwjgl) {
            log.info("Running with old lwjgl, using xvfb transformer");
            transformers.add(new XvfbLwjglTransformer());
        }
    }

}
