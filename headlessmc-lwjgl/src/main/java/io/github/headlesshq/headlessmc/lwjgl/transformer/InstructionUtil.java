package io.github.headlesshq.headlessmc.lwjgl.transformer;

import lombok.experimental.UtilityClass;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * Utility for dealing with Instructions.
 */
@UtilityClass
public final class InstructionUtil {
    public static AbstractInsnNode loadType(Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT:
            case Type.FLOAT:
            case Type.LONG:
            case Type.DOUBLE:
            case Type.CHAR:
            case Type.VOID:
                return new FieldInsnNode(GETSTATIC,
                                         getWrapper(type).getInternalName(),
                                         "TYPE",
                                         Type.getDescriptor(Class.class));
            default:
                return new LdcInsnNode(type);
        }
    }

    public static MethodInsnNode box(Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
            case Type.CHAR:
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT:
            case Type.FLOAT:
            case Type.LONG:
            case Type.DOUBLE:
                Type wrap = getWrapper(type);
                return new MethodInsnNode(INVOKESTATIC, wrap.getInternalName(),
                                          "valueOf", "(" + type.getDescriptor()
                                              + ")" + wrap.getDescriptor(),
                                          false);
            default:
                return null;
        }
    }

    public static Type getWrapper(Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
                return Type.getType("Ljava/lang/Boolean;");
            case Type.CHAR:
                return Type.getType("Ljava/lang/Char;");
            case Type.BYTE:
                return Type.getType("Ljava/lang/Byte;");
            case Type.SHORT:
                return Type.getType("Ljava/lang/Short;");
            case Type.INT:
                return Type.getType("Ljava/lang/Integer;");
            case Type.FLOAT:
                return Type.getType("Ljava/lang/Float;");
            case Type.LONG:
                return Type.getType("Ljava/lang/Long;");
            case Type.DOUBLE:
                return Type.getType("Ljava/lang/Double;");
            case Type.VOID:
                return Type.getType("Ljava/lang/Void;");
            default:
                return type;
        }
    }

    public static boolean isPrimitive(Type type) {
        return getWrapper(type) != type;
    }

    public static InsnList unbox(Type type) {
        String name;
        InsnList il = new InsnList();
        switch (type.getSort()) {
            case Type.BOOLEAN:
                name = "booleanValue";
                break;
            case Type.CHAR:
                name = "charValue";
                break;
            case Type.BYTE:
                name = "byteValue";
                break;
            case Type.SHORT:
                name = "shortValue";
                break;
            case Type.INT:
                name = "intValue";
                break;
            case Type.FLOAT:
                name = "floatValue";
                break;
            case Type.LONG:
                name = "longValue";
                break;
            case Type.DOUBLE:
                name = "doubleValue";
                break;
            case Type.VOID:
                il.add(new InsnNode(POP));
                return il;
            default:
                il.add(new TypeInsnNode(CHECKCAST, type.getInternalName()));
                return il;
        }

        String o = getWrapper(type).getInternalName();
        String s = "()" + type.getDescriptor();
        il.add(new TypeInsnNode(CHECKCAST, o));
        il.add(new MethodInsnNode(INVOKEVIRTUAL, o, name, s));
        return il;
    }

    public static VarInsnNode loadParam(Type type, int var) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
            case Type.CHAR:
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT:
                return new VarInsnNode(ILOAD, var);
            case Type.FLOAT:
                return new VarInsnNode(FLOAD, var);
            case Type.LONG:
                return new VarInsnNode(LLOAD, var);
            case Type.DOUBLE:
                return new VarInsnNode(DLOAD, var);
            case Type.VOID:
                throw new IllegalArgumentException("Can't load VOID type!");
            default:
                return new VarInsnNode(ALOAD, var);
        }
    }

    public static InsnNode makeReturn(Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
            case Type.CHAR:
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT:
                return new InsnNode(IRETURN);
            case Type.FLOAT:
                return new InsnNode(FRETURN);
            case Type.LONG:
                return new InsnNode(LRETURN);
            case Type.DOUBLE:
                return new InsnNode(DRETURN);
            case Type.VOID:
                return new InsnNode(RETURN);
            default:
                return new InsnNode(ARETURN);
        }
    }

}
