package me.earth.headlessmc.launcher.mods.files;

import lombok.Data;
import me.earth.headlessmc.launcher.api.Platform;
import me.earth.headlessmc.launcher.mods.ModdableGame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ModFileReader {
    List<ModFile> read(Supplier<Integer> id, Path path) throws IOException;

}
