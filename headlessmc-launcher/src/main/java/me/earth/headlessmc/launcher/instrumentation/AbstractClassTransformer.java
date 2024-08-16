package me.earth.headlessmc.launcher.instrumentation;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
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
        byte @Nullable [] transformedClassBytes = maybeTransform(stream);
        if (transformedClassBytes != null) {
            return EntryStream.of(transformedClassBytes, stream.getTargets(), stream.getEntry());
        }

        return stream;
    }

    public byte @Nullable [] maybeTransform(EntryStream stream) throws IOException {
        if (matches(stream)) {
            log.debug("Reading " + stream.getEntry().getName());
            ClassReader reader = new ClassReader(stream.getStream());
            ClassNode node = new ClassNode();
            reader.accept(node, 0); // TODO: do we want parserOptions?
            this.transform(node);
            ClassWriter writer = getEntryClassWriter(stream);
            log.debug("Writing transformed class: " + node.name);
            node.accept(writer);
            setRun(true);
            return writer.toByteArray();
        }

        return null;
    }

    public EntryClassWriter getEntryClassWriter(EntryStream entry) throws IOException {
        return new EntryClassWriter(entry);
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
