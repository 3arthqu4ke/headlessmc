package me.earth.headlessmc.launcher.version;

import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.launcher.os.OS;

public interface Library extends HasName {
    String getPath(OS os);

    Rule getRule();

    Extractor getExtractor();

    String getUrl(String path);

    boolean isNativeLibrary();

}
