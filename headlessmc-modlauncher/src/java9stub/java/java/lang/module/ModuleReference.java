package java.lang.module;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

/**
 * @since 9
 */
@SuppressWarnings({"unused", "Since15"})
public abstract class ModuleReference {
    public abstract ModuleReader open() throws IOException;

    public final ModuleDescriptor descriptor() {
        throw new RuntimeException("stub");
    }

    public final Optional<URI> location() {
        throw new RuntimeException("stub");
    }

}
