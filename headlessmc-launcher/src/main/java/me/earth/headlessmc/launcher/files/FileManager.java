package me.earth.headlessmc.launcher.files;

import lombok.CustomLog;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

// TODO: move to Paths?!?!?!?!?!?!
// TODO: Why were we using Files in the first place?
@CustomLog
public class FileManager {
    private final String base;

    public FileManager(String base) {
        this.base = base;
    }

    public static FileManager mkdir(String path) {
        File file = Paths.get(path).toFile();
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
        return new FileManager(file.getAbsolutePath());
    }

    public File getBase() {
        return get(base, true, false);
    }

    /**
     * Returns a directory for the given path relative to {@link #getBase()}. If
     * the directory doesn't exist it will be created.
     *
     * @param path the parts of the path, joined on {@link File#separator}.
     * @return a {@link File} with the given path.
     * @throws FileException if the file can't be created.
     */
    public File getDir(String... path) {
        return get(true, true, path);
    }

    /**
     * Returns a file for the given path relative to {@link #getBase()}. If the
     * file doesn't exist it will be created.
     *
     * @param path the parts of the path, joined on {@link File#separator}.
     * @return a {@link File} with the given path.
     * @throws FileException if the file can't be created.
     */
    public File create(String... path) {
        return get(false, true, path);
    }

    /**
     * Returns a file for the given path relative to {@link #getBase()}. If the
     * file doesn't exist it will be created.
     *
     * @param path  the parts of the path, joined on {@link File#separator}.
     * @param isDir if the file is a directory.
     * @return a {@link File} with the given path.
     * @throws FileException if the file can't be created.
     */
    public File get(boolean isDir, boolean mk, String... path) {
        return get(getBase().getAbsolutePath(), isDir, mk, path);
    }

    /**
     * Returns a file for the given path relative to the given base. If the file
     * doesn't exist it will be created.
     *
     * @param base  the base path the returned file will be relative to.
     * @param path  the parts of the path, joined on {@link File#separator}.
     * @param isDir if the file is a directory.
     * @return a {@link File} with the given path.
     * @throws FileException if the file can't be created.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File get(String base, boolean isDir, boolean mk, String... path) {
        File file = new File(base, String.join(File.separator, path));
        log.debug("Checking file: " + file.getAbsolutePath());
        if (mk && !file.exists()) {
            log.debug("File " + file + " doesn't exist, creating it...");
            if (isDir) {
                file.mkdirs();
            } else {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO: handle this better
                    throw new FileException("Can't create " + file + " : "
                                                + e.getMessage());
                }
            }
        }

        return file;
    }

    public File[] listFiles() {
        File[] result = getBase().listFiles();
        return result == null ? new File[0] : result;
    }

    public FileManager createRelative(String... base) {
        getDir(base);
        return relative(base);
    }

    public FileManager relative(String... base) {
        return new FileManager(this.base
                                   + File.separator
                                   + String.join(File.separator, base));
    }

    public String getPath() {
        return getBase().getAbsolutePath();
    }

}
