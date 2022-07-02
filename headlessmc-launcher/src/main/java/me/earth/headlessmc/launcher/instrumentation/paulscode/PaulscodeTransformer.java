package me.earth.headlessmc.launcher.instrumentation.paulscode;

import lombok.CustomLog;
import me.earth.headlessmc.launcher.instrumentation.AbstractClassTransformer;
import me.earth.headlessmc.launcher.instrumentation.Target;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.HashSet;
import java.util.Set;

/**
 * Minecrafts SoundManager spams us with some errorMessages if we use the -lwjgl
 * flag. This Transformer removes those messages.
 */
@CustomLog
public class PaulscodeTransformer extends AbstractClassTransformer {
    private final Set<String> methods = new HashSet<>();

    public PaulscodeTransformer() {
        super("paulscode/sound/Library");
        methods.add("message(Ljava/lang/String;)V");
        methods.add("importantMessage(Ljava/lang/String;)V");
        methods.add("errorCheck(ZLjava/lang/String;)V");
        methods.add("errorMessage(Ljava/lang/String;)V");
        methods.add("printStackTrace(Ljava/lang/Exception;)V");
    }

    @Override
    protected void transform(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            String desc = mn.name + mn.desc;
            if (methods.contains(desc)) {
                log.debug("Clearing " + desc);
                mn.instructions = new InsnList();
                mn.instructions.add(new InsnNode(Opcodes.RETURN));
            }
        }
    }

    @Override
    public boolean matches(Target target) {
        String path = target.getPath().toLowerCase();
        return path.contains("paulscode") && path.contains("soundsystem");
    }

}
