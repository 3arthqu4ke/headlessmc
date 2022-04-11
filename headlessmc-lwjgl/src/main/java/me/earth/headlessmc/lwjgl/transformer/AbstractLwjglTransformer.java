package me.earth.headlessmc.lwjgl.transformer;

import me.earth.headlessmc.lwjgl.api.Redirection;
import me.earth.headlessmc.lwjgl.api.RedirectionApi;
import me.earth.headlessmc.lwjgl.api.Transformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Modifier;

import static org.objectweb.asm.Opcodes.*;

public abstract class AbstractLwjglTransformer implements Transformer {
    @Override
    public void transform(ClassNode cn) {
        if (cn.module != null) {
            cn.module.visitRequire("headlessmc.lwjgl", ACC_MANDATED, null);
            cn.module.access |= ACC_OPEN;
        }
    }

    protected Type injectRedirection(ClassNode cn, MethodNode mn, InsnList il) {
        boolean isStatic = Modifier.isStatic(mn.access);
        if (isStatic) {
            il.add(new LdcInsnNode(Type.getType("L" + cn.name + ";")));
        } else {
            il.add(new VarInsnNode(ALOAD, 0));
        }

        il.add(new LdcInsnNode("L" + cn.name + ";" + mn.name + mn.desc));
        Type returnType = Type.getReturnType(mn.desc);
        il.add(InstructionUtil.loadType(returnType));
        loadArgArray(mn.desc, il, isStatic);

        il.add(new MethodInsnNode(
            INVOKESTATIC, Type.getInternalName(RedirectionApi.class),
            Redirection.METHOD_NAME, Redirection.METHOD_DESC));

        il.add(InstructionUtil.unbox(returnType));
        return returnType;
    }

    protected void loadArgArray(String desc, InsnList il, boolean isStatic) {
        Type[] args = Type.getArgumentTypes(desc);
        il.add(new LdcInsnNode(args.length));
        il.add(new TypeInsnNode(ANEWARRAY, Type.getInternalName(Object.class)));
        for (int i = 0, v = isStatic ? 0 : 1; i < args.length; i++, v++) {
            il.add(new InsnNode(DUP));

            Type type = args[i];
            il.add(new LdcInsnNode(i));
            il.add(InstructionUtil.loadParam(type, v));
            if (type.getSort() == Type.DOUBLE || type.getSort() == Type.LONG) {
                // double and long take up two registers, so we skip one
                v++;
            }

            MethodInsnNode boxing = InstructionUtil.box(type);
            if (boxing != null) {
                il.add(boxing);
            }

            il.add(new InsnNode(AASTORE));
        }
    }

}
