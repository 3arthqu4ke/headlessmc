package me.earth.headlessmc.launcher.instrumentation;

import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.instrumentation.log4j.Patchers;
import me.earth.headlessmc.launcher.instrumentation.lwjgl.HmcLwjglTransformer;
import me.earth.headlessmc.launcher.instrumentation.paulscode.PaulscodeTransformer;

import java.util.ArrayList;

@UtilityClass
public class InstrumentationHelper {
    public static final String RUNTIME_JAR = "headlessmc-runtime.jar";
    public static final String LWJGL_JAR = "headlessmc-lwjgl.jar";

    public static Instrumentation create(FileManager fileManager,
                                         boolean lwjgl,
                                         boolean commands,
                                         boolean jndi,
                                         boolean lookup,
                                         boolean paulscode) {
        val transformers = new ArrayList<Transformer>(6);
        if (lwjgl) {
            transformers.add(new HmcLwjglTransformer());
            transformers.add(new ResourceExtractor(fileManager, LWJGL_JAR));
        }

        if (paulscode) {
            transformers.add(new PaulscodeTransformer());
        }

        if (jndi) {
            transformers.add(Patchers.JNDI);
        }

        if (commands) {
            transformers.add(new ResourceExtractor(fileManager, RUNTIME_JAR));
        }

        if (lookup) {
            transformers.add(Patchers.LOOKUP);
        }

        return new Instrumentation(transformers, fileManager.getBase());
    }

}
