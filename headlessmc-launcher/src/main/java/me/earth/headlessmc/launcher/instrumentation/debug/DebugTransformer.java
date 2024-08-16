package me.earth.headlessmc.launcher.instrumentation.debug;

import me.earth.headlessmc.launcher.instrumentation.AbstractClassTransformer;
import me.earth.headlessmc.launcher.instrumentation.EntryStream;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class DebugTransformer extends AbstractClassTransformer {
    public DebugTransformer() {
        super("");
    }

    @Override
    protected void transform(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if (Modifier.isAbstract(mn.access) || Modifier.isNative(mn.access) || "<init>".equals(mn.name) || "<clinit>".equals(mn.name)) {
                continue;
            }

            AbstractInsnNode node = mn.instructions.getFirst();
            AbstractInsnNode last = null;
            while (node != null) {
                if (node instanceof MethodInsnNode) {
                    InsnList debug = new InsnList();
                    //debug.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(System.class), "out", Type.getDescriptor(PrintStream.class)));
                    debug.add(new LdcInsnNode("Calling: " + ((MethodInsnNode) node).owner + "." + ((MethodInsnNode) node).name + ((MethodInsnNode) node).desc
                     + " from " + cn.name + "." + mn.name + mn.desc));
                    //debug.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(PrintStream.class), "println", "(Ljava/lang/String;)V", false));
                    debug.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(DebugTransformer.class), "println", "(Ljava/lang/String;)V", false));
                    if (last == null) {
                        mn.instructions.insert(debug);
                    } else {
                        mn.instructions.insert(last, debug);
                    }
                }

                last = node;
                node = node.getNext();
            }

            InsnList debug = new InsnList();
            //debug.add(new FieldInsnNode(GETSTATIC, Type.getInternalName(System.class), "out", Type.getDescriptor(PrintStream.class)));
            debug.add(new LdcInsnNode(cn.name + "." + mn.name + mn.desc));
            //debug.add(new MethodInsnNode(INVOKEVIRTUAL, Type.getInternalName(PrintStream.class), "println", "(Ljava/lang/String;)V", false));
            debug.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(DebugTransformer.class), "println", "(Ljava/lang/String;)V", false));
            mn.instructions.insert(debug);
            //mn.visitMaxs(0, 0);
        }
    }

    public static void println(String string) throws InterruptedException {
        System.out.println(string);
        Thread.sleep(50);
    }

    @Override
    protected boolean matches(EntryStream stream) {
        return !stream.getEntry().getName().contains("joptsimple");
    }

}
