package io.github.headlesshq.headlessmc.lwjgl.transformer;

import lombok.experimental.UtilityClass;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

@UtilityClass
public class AsmUtil {
    public static ClassNode read(byte[] clazz, int... flags) {
        ClassNode result = new ClassNode();
        ClassReader reader = new ClassReader(clazz);
        reader.accept(result, toFlag(flags));
        return result;
    }

    public static byte[] write(ClassNode classNode, int... flags) {
        ClassWriter writer = new ClassWriter(toFlag(flags));
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private static int toFlag(int... flags) {
        int flag = 0;
        for (int f : flags) {
            flag |= f;
        }

        return flag;
    }

}
