package java.lang.module;

import java.nio.file.Path;
import java.util.Set;

/**
 * @since 9
 */
@SuppressWarnings({"unused", "Since15"})
public interface ModuleFinder {
    Set<ModuleReference> findAll();

    static ModuleFinder of(Path... entries) {
        throw new IllegalStateException("Stub");
    }

}
