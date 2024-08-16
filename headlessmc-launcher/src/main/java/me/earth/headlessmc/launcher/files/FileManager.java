package me.earth.headlessmc.launcher.files;

import lombok.CustomLog;
import lombok.Setter;
import me.earth.headlessmc.launcher.util.IOConsumer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

// TODO: move to Paths?!?!?!?!?!?!
// TODO: Why were we using Files in the first place?
@CustomLog
public class FileManager {
    @Setter
    private static Function<String, FileManager> factory = FileManager::new;
    private final String base;

    // @Deprecated
    public FileManager(String base) {
        this.base = base;
    }

    public static FileManager forPath(String path) {
        return factory.apply(path);
    }

    public static FileManager mkdir(String path) {
        File file = Paths.get(path).toFile();
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
        return factory.apply(file.getAbsolutePath());
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
        log.finest("Checking file: " + file.getAbsolutePath());
        if (mk && !file.exists()) {
            log.finest("File " + file + " doesn't exist, creating it...");
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
        return listFiles(getBase());
    }

    public FileManager createRelative(String... base) {
        getDir(base);
        return relative(base);
    }

    public FileManager relative(String... base) {
        return factory.apply(this.base + File.separator + String.join(File.separator, base));
    }

    public String getPath() {
        return getBase().getAbsolutePath();
    }

    public File[] listFiles(File file) {
        File[] result = file.listFiles();
        return result == null ? new File[0] : result;
    }

    public void delete(File file) throws IOException {
        iterate(file, f -> Files.delete(f.toPath()));
    }

    protected void iterate(File file, IOConsumer<File> action) throws IOException {
        for (File content : listFiles(file)) {
            if (content.isDirectory()) {
                iterate(content, action);
            } else {
                action.accept(content);
            }
        }

        action.accept(file);
    }

}
