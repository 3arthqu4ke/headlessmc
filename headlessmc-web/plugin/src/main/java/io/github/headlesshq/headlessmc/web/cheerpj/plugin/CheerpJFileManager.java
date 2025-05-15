package io.github.headlesshq.headlessmc.web.cheerpj.plugin;

import io.github.headlesshq.headlessmc.launcher.files.FileManager;

import java.io.File;
import java.io.IOException;

public class CheerpJFileManager extends FileManager {
    public CheerpJFileManager(String base) {
        super(base);
    }

    @Override
    public void delete(File file) throws IOException {
        // CheerpJ does not seem to support java.nio.files.Files.delete yet
        //noinspection ResultOfMethodCallIgnored
        iterate(file, File::delete);
    }

}
