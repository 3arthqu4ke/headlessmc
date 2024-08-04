package java.lang;

import java.lang.module.Configuration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @since 9
 */
@SuppressWarnings({"unused", "Since15"})
public final class ModuleLayer {
    public Configuration configuration() {
        throw new IllegalStateException("stub");
    }

    public Optional<Module> findModule(String name) {
        throw new IllegalStateException("stub");
    }

    public static Controller defineModules(Configuration cf, List<ModuleLayer> parentLayers, Function<String, ClassLoader> clf) {
        throw new IllegalStateException("stub");
    }

    public static ModuleLayer boot() {
        throw new IllegalStateException("stub");
    }

    public static final class Controller {
        public ModuleLayer layer() {
            throw new IllegalStateException("stub");
        }
    }

    void bindToLoader(ClassLoader loader) {
        throw new IllegalStateException("stub");
    }

}
