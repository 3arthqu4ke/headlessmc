package me.earth.headlessmc.launcher.files;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.version.Version;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the directories the HeadlessMc Launcher will work in.
 */
@Getter
@Setter
@AllArgsConstructor
public class LauncherConfig {
    /**
     * Supplies the config to check the {@link LauncherProperties#GAME_DIR_FOR_EACH_VERSION} property from.
     * Also supplies {@link #getFileManager()}.
     */
    private ConfigService config;
    /**
     * The .minecraft directory in which we store assets, libraries and versions.
     */
    private FileManager mcFiles;
    /**
     * The directory in which the game will run.
     */
    private FileManager gameDir;

    /**
     * The directory HeadlessMc configuration files, logs and others reside in.
     * Usually the folder HeadlessMC where the launcher has been started.
     * This FileManager is usually supplied from the {@link #config}.
     *
     * @return The directory for HeadlessMc files.
     * @see #setFileManager(FileManager)
     */
    public FileManager getFileManager() {
        return config.getFileManager();
    }

    /**
     * Sets the FileManager to use for HeadlessMc.
     *
     * @param fileManager the new file manager to use.
     * @see #getFileManager()
     */
    public void setFileManager(FileManager fileManager) {
        config.setFileManager(fileManager);
    }

    /**
     * Gets the directory to run the game in for a specific version.
     * Usually this is just {@link #gameDir}, but if {@link LauncherProperties#GAME_DIR_FOR_EACH_VERSION} is enabled,
     * a game directory for each version will be created inside {@link #gameDir}.
     *
     * @param version the version to get a game directory for.
     * @return a FileManager managing the game dir for this version.
     */
    public FileManager getGameDir(Version version) {
        return getGameDir(version.getName());
    }

    /**
     * Gets the directory to run the game in for a specific name.
     * Usually this is just {@link #gameDir}, but if {@link LauncherProperties#GAME_DIR_FOR_EACH_VERSION} is enabled,
     * a relative directory for the given name will be created inside {@link #gameDir}.
     * If name is {@code null} the default game directory will be used instead.
     *
     * @param name the name to get a game directory for.
     * @return a FileManager managing the game dir for this version.
     */
    public FileManager getGameDir(@Nullable String name) {
        if (name != null && config.getConfig().get(LauncherProperties.GAME_DIR_FOR_EACH_VERSION, false)) {
            return gameDir.createRelative(name);
        }

        return gameDir;
    }

}
