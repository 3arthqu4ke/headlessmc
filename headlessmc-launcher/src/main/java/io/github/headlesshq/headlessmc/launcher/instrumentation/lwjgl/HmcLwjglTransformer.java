package io.github.headlesshq.headlessmc.launcher.instrumentation.lwjgl;

import io.github.headlesshq.headlessmc.launcher.instrumentation.AbstractClassTransformer;
import io.github.headlesshq.headlessmc.launcher.instrumentation.EntryStream;
import io.github.headlesshq.headlessmc.launcher.instrumentation.InstrumentationHelper;
import io.github.headlesshq.headlessmc.launcher.instrumentation.Target;
import io.github.headlesshq.headlessmc.lwjgl.api.Transformer;
import io.github.headlesshq.headlessmc.lwjgl.transformer.LwjglTransformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.Locale;

public class HmcLwjglTransformer extends AbstractClassTransformer {
    private final Transformer transformer = new LwjglTransformer();

    public HmcLwjglTransformer() {
        super(null);
    }

    @Override
    public void transform(ClassNode classNode) {
        transformer.transform(classNode);
    }

    @Override
    protected boolean matches(EntryStream stream) {
        return stream.getEntry().getName().toLowerCase(Locale.ENGLISH).contains("lwjgl")
            && stream.getEntry().getName().endsWith(".class")
            || stream.getEntry().getName().endsWith("module-info.class");
    }

    @Override
    public boolean matches(Target target) {
        return target.getPath().toLowerCase(Locale.ENGLISH).contains("lwjgl")
            && !target.getPath().endsWith(InstrumentationHelper.LWJGL_JAR);
    }

}
