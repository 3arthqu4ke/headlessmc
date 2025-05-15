package io.github.headlesshq.headlessmc.launcher.mods.files;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;

public interface ModFileReader {
    List<ModFile> read(Supplier<Integer> id, Path path) throws IOException;

}
