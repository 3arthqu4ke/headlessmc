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
        throw new RuntimeException("stub");
    }

    public Optional<Module> findModule(String name) {
        throw new RuntimeException("stub");
    }

    public static Controller defineModules(Configuration cf, List<ModuleLayer> parentLayers, Function<String, ClassLoader> clf) {
        throw new RuntimeException("stub");
    }

    public static ModuleLayer boot() {
        throw new RuntimeException("stub");
    }

    public static final class Controller {
        public ModuleLayer layer() {
            throw new RuntimeException("stub");
        }
    }

    void bindToLoader(ClassLoader loader) {
        throw new RuntimeException("stub");
    }

}
