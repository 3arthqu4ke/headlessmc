package me.earth.headlessmc.lwjgl.transformer;

import me.earth.headlessmc.lwjgl.api.Redirection;
import me.earth.headlessmc.lwjgl.api.RedirectionApi;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.*;

/**
 * A given {@link ClassNode} will be transformed in the following ways:
 * <p>-if it's a module a {@code requires headlessmc.lwjgl} will be added.
 * <p>-the no-args constructor will be made public or created if necessary
 * <p>-Every method body will have its code removed and will call
 * {@link RedirectionApi#invoke(Object, String, Class, Object...)}.
 * <p>-the constructor will keep its body, a {@link RedirectionApi#invoke(Object,
 * String, Class, Object...)} will be added before all RETURN instructions in
 * the constructor. Class casts in constructors will be redirected.
 * <p>-if the class is abstract and not an interface the abstract modifier
 * will be removed.
 * <p>-All abstract and native methods will be turned into normal methods, with
 * their body transformed as described above.
 */
public class LwjglTransformer extends AbstractLwjglTransformer {
    @Override
    public void transform(ClassNode cn) {
        super.transform(cn);
        if ((cn.access & ACC_MODULE) == 0) {
            patchClass(cn, (cn.access & ACC_INTERFACE) != 0);
            // make all non-static fields non-final
            for (FieldNode fn : cn.fields) {
                if ((fn.access & ACC_STATIC) == 0) {
                    fn.access &= ~ACC_FINAL;
                }
            }
        }
    }

    private void patchClass(ClassNode cn, boolean isInterface) {
        boolean shouldAddNoArgsCtr = true;
        // TODO: while we can implement all abstract methods which are directly
        //  present in the class, methods inherited from an interface or another
        //  abstract class may pose a problem.
        //  We could at least warn every time a formerly abstract class is being
        //  instantiated?
        if (!isInterface) {
            cn.access &= ~ACC_ABSTRACT;
        }

        for (MethodNode mn : cn.methods) {
            if (mn.name.equals("<init>") && mn.desc.equals("()V")) {
                // TODO: check this
                mn.access |= ACC_PUBLIC;
                mn.access &= ~(ACC_PROTECTED | ACC_PRIVATE);
                shouldAddNoArgsCtr = false;
            }

            if (!isInterface
                || (mn.access & ACC_STATIC) != 0
                || (mn.access & ACC_ABSTRACT) == 0) {
                redirect(mn, cn);
            }
        }

        // TODO: super class containing lwjgl is no guarantee for it to
        //  have a default constructor!
        if (shouldAddNoArgsCtr
            && !isInterface
            && (cn.superName == null
            || cn.superName.toLowerCase().contains("lwjgl")
            || cn.superName.equals(Type.getInternalName(Object.class)))) {
            // Add NoArgs Constructor for the ObjectRedirection
            MethodNode mn = new MethodNode(ACC_PUBLIC, "<init>", "()V",
                                           null, new String[0]);
            InsnList il = new InsnList();
            mn.instructions = il;
            il.add(new VarInsnNode(ALOAD, 0));
            il.add(new MethodInsnNode(INVOKESPECIAL, cn.superName, "<init>",
                                      "()V", false));
            injectRedirection(cn, mn, il);
            il.add(new InsnNode(RETURN));
            cn.methods.add(mn);
            mn.visitMaxs(0, 0);
        }
    }

    private void redirect(MethodNode mn, ClassNode cn) {
        if ("<init>".equals(mn.name) && mn.desc.endsWith(")V")) {
            // There's not really a way to determine when this() or super()
            // is called in a constructor, because of that we'll just inject
            // the RedirectionManager at every RETURN instruction.
            // This hasn't become a problem, yet...
            AbstractInsnNode insnNode = mn.instructions.getFirst();
            while (insnNode != null) {
                if (insnNode instanceof TypeInsnNode
                    && insnNode.getOpcode() == CHECKCAST) {
                    mn.instructions.insertBefore(
                        insnNode, redirectCast((TypeInsnNode) insnNode));
                } else if (insnNode.getOpcode() == RETURN) {
                    InsnList il = new InsnList();
                    injectRedirection(cn, mn, il);
                    mn.instructions.insertBefore(insnNode, il);
                }

                insnNode = insnNode.getNext();
            }
        } else {
            InsnList il = new InsnList();
            il.add(InstructionUtil.makeReturn(injectRedirection(cn, mn, il)));
            mn.instructions = il;
        }

        mn.tryCatchBlocks = new ArrayList<>();
        mn.localVariables = new ArrayList<>();
        mn.parameters = new ArrayList<>();
        mn.access = mn.access & ~ACC_NATIVE & ~ACC_ABSTRACT;
    }

    private InsnList redirectCast(TypeInsnNode cast) {
        InsnList il = new InsnList();
        il.add(new LdcInsnNode(Redirection.CAST_PREFIX + cast.desc));
        // TODO: does this really cover all cases?
        if (cast.desc.startsWith("[")) {
            il.add(new LdcInsnNode(Type.getType(cast.desc)));
        } else {
            il.add(new LdcInsnNode(Type.getType("L" + cast.desc + ";")));
        }

        loadArgArray("()V", il, false); // create empty array
        il.add(new MethodInsnNode(
            INVOKESTATIC, Type.getInternalName(RedirectionApi.class),
            Redirection.METHOD_NAME, Redirection.METHOD_DESC));
        return il;
    }

}
