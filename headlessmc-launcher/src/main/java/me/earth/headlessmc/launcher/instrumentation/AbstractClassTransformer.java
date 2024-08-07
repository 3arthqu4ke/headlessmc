package me.earth.headlessmc.launcher.instrumentation;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;

@CustomLog
@RequiredArgsConstructor
public abstract class AbstractClassTransformer extends AbstractTransformer {
    private final String className;

    @Override
    public EntryStream transform(EntryStream stream) throws IOException {
        if (matches(stream)) {
            log.debug("Reading " + stream.getEntry().getName());
            ClassReader reader = new ClassReader(stream.getStream());
            ClassNode node = new ClassNode();
            reader.accept(node, 0); // TODO: do we want parserOptions?
            this.transform(node);
            ClassWriter writer = new EntryClassWriter(stream);
            log.debug("Writing transformed class: " + node.name);
            node.accept(writer);
            setRun(true);
            return EntryStream.of(writer.toByteArray(),
                                  stream.getTargets(),
                                  stream.getEntry());
        }

        return stream;
    }

    protected boolean matches(EntryStream stream) {
        if (stream.getEntry().getName().endsWith(".class")) {
            return stream.getEntry()
                         .getName()
                         .substring(0, stream.getEntry().getName().length() - 6)
                         .equals(className);
        }

        return false;
    }

    protected abstract void transform(ClassNode cn);

}
