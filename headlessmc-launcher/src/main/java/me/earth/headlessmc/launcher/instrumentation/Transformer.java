package me.earth.headlessmc.launcher.instrumentation;

import java.io.IOException;
import java.util.List;

public interface Transformer {
    boolean hasRun();

    default List<Target> transform(List<Target> targets)
        throws IOException {
        return targets;
    }

    default boolean matches(Target target) {
        return false;
    }

    default EntryStream transform(EntryStream stream) throws IOException {
        return stream;
    }

}
