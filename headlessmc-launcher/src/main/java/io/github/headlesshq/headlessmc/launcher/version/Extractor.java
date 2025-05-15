package io.github.headlesshq.headlessmc.launcher.version;

import io.github.headlesshq.headlessmc.launcher.files.FileManager;

import java.io.IOException;

public interface Extractor {
    Extractor NO_EXTRACTION = (from, fileManager) -> {
    };

    void extract(String from, FileManager fileManager) throws IOException;

    default boolean isExtracting() {
        return false;
    }

    default boolean shouldExtract(String name) {
        return false;
    }

}
