package me.earth.headlessmc.lwjgl.api;

import org.objectweb.asm.tree.ClassNode;

public interface Transformer {
    void transform(ClassNode classNode);

}
