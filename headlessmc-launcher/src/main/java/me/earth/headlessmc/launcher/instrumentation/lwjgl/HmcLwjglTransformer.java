package me.earth.headlessmc.launcher.instrumentation.lwjgl;

import me.earth.headlessmc.launcher.instrumentation.AbstractClassTransformer;
import me.earth.headlessmc.launcher.instrumentation.EntryStream;
import me.earth.headlessmc.launcher.instrumentation.InstrumentationHelper;
import me.earth.headlessmc.launcher.instrumentation.Target;
import me.earth.headlessmc.lwjgl.api.Transformer;
import me.earth.headlessmc.lwjgl.transformer.LwjglTransformer;
import org.objectweb.asm.tree.ClassNode;

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
        return stream.getEntry().getName().toLowerCase().contains("lwjgl")
            && stream.getEntry().getName().endsWith(".class")
            || stream.getEntry().getName().endsWith("module-info.class");
    }

    @Override
    public boolean matches(Target target) {
        return target.getPath().toLowerCase().contains("lwjgl")
            && !target.getPath().endsWith(InstrumentationHelper.LWJGL_JAR);
    }

}
