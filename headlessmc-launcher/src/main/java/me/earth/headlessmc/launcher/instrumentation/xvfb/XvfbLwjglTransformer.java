package me.earth.headlessmc.launcher.instrumentation.xvfb;

import lombok.CustomLog;
import me.earth.headlessmc.launcher.instrumentation.AbstractClassTransformer;
import me.earth.headlessmc.launcher.instrumentation.InstrumentationHelper;
import me.earth.headlessmc.launcher.instrumentation.Target;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Locale;

/**
 * Lwjgl 2.9.4 (Mc 1.12.2) crashes in Github actions with xvfb at getAvailableDisplayModes,
 * because the array returned by XRandR.getResolutions is empty.
 * This patches that by always using XF86VIDMODE instead of XRANDR.
 */
@CustomLog
public class XvfbLwjglTransformer extends AbstractClassTransformer {
    public XvfbLwjglTransformer() {
        super("org/lwjgl/opengl/LinuxDisplay");
    }

    @Override
    protected void transform(ClassNode cn) {
        // TODO: Actually isXrandrSupported can also be overriden by setting the system property LWJGL_DISABLE_XRANDR
        // but this would theoretically cover the case when XF86VIDMODE is not supported?
        for (MethodNode mn : cn.methods) {
            if ("getBestDisplayModeExtension".equals(mn.name) && "()I".equals(mn.desc)) {
                for (AbstractInsnNode insnNode : mn.instructions) {
                    if (insnNode instanceof IntInsnNode && insnNode.getOpcode() == Opcodes.BIPUSH) {
                        IntInsnNode intInsnNode = (IntInsnNode) insnNode;
                        if (intInsnNode.operand == 10) { // private static final int XRANDR = 10;
                            log.info("Found BI_PUSH XRANDR, replacing with XF86VIDMODE");
                            intInsnNode.operand = 11; // XF86VIDMODE, idk lets just try this?
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean matches(Target target) {
        return target.getPath().toLowerCase(Locale.ENGLISH).contains("lwjgl")
                && !target.getPath().endsWith(InstrumentationHelper.LWJGL_JAR);
    }

}
