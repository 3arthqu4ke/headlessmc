package java.lang.module;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * @since 9
 */
@SuppressWarnings({"unused", "Since15"})
public class Configuration {
    public Configuration resolve(ModuleFinder before, ModuleFinder after, Collection<String> roots) {
        throw new IllegalStateException("stub");
    }

    public Optional<ResolvedModule> findModule(String name) {
        throw new IllegalStateException("stub");
    }

    public Set<ResolvedModule> modules() {
        throw new IllegalStateException("stub");
    }

}
