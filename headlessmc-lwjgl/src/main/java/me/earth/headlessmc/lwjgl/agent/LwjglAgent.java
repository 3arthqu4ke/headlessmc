package me.earth.headlessmc.lwjgl.agent;

import me.earth.headlessmc.lwjgl.transformer.LwjglTransformer;
import me.earth.headlessmc.lwjgl.api.Transformer;
import me.earth.headlessmc.lwjgl.transformer.AsmUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * A JavaAgent calling the {@link LwjglTransformer}.
 */
public class LwjglAgent implements ClassFileTransformer {
    private final Transformer transformer = new LwjglTransformer();

    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer)
        throws IllegalClassFormatException {
        if (className.contains("lwjgl")) {
            ClassNode node = AsmUtil.read(classfileBuffer);
            transformer.transform(node);
            // TODO: make writer.getClassLoader() return the given loader?
            return AsmUtil.write(node, ClassWriter.COMPUTE_FRAMES);
        }

        return classfileBuffer;
    }

    public static void premain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new LwjglAgent());
    }

    public static void agentmain(String args, Instrumentation instrumentation) {
        instrumentation.addTransformer(new LwjglAgent());
    }

}
