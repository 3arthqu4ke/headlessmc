package me.earth.headlessmc.launcher.server;

import lombok.CustomLog;
import lombok.Data;
import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.launcher.mods.ModdableGame;
import me.earth.headlessmc.launcher.api.Platform;
import me.earth.headlessmc.os.OS;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.stream.Stream;

@Data
@CustomLog
public class Server implements HasName, HasId, ModdableGame {
    public static final String DEFAULT_JAR = "server.jar";

    private final Path path;
    private final String name;
    private final ServerVersion version;
    private final int id;

    public Path getEula(boolean inMemory) {
        return inMemory
                ? Paths.get("eula.txt") // if we launch in memory the working dir is here
                : path.resolve("eula.txt");
    }

    public boolean hasEula(boolean inMemory) {
        return Files.exists(getEula(inMemory));
    }

    public Path getExecutable(OS os) {
        Path jar = path.resolve(DEFAULT_JAR);
        if (!Files.exists(jar)) {
            if ("fabric".equalsIgnoreCase(version.getServerType().getName())) {
                jar = path.resolve("fabric-server-launch.jar");
            } else if (version.getServerType().getName().toLowerCase(Locale.ENGLISH).contains("forge")) {
                Path runFile = os.getType() == OS.Type.WINDOWS
                        ? path.resolve("run.bat")
                        : path.resolve("run.sh");
                if (Files.exists(runFile)) {
                    return runFile;
                }

                String name = getName(version.getServerType(), version.getVersion(), version.getTypeVersion())
                        .substring(1); // prevents capitalization issues like Forge vs forge
                try (Stream<Path> files = Files.list(path)) {
                    Path path = files.filter(f -> f.toString().endsWith(".jar"))
                            .filter(f -> f.getFileName().toString().contains(name))
                            .findFirst()
                            .orElse(null);
                    if (path != null) {
                        return path;
                    }
                } catch (IOException e) {
                    log.error(e);
                    return path.resolve(DEFAULT_JAR);
                }
            }
        }

        return jar;
    }

    public boolean hasCustomName() {
        return !name.startsWith(getName(version.getServerType(), version.getVersion(), version.getTypeVersion()));
    }

    public static String getName(ServerType type, String version, @Nullable String typeVersion) {
        return type.getName() + "-" + version + (typeVersion == null ? "" : "-" + typeVersion);
    }

    @Override
    public Path getModsDirectory() {
        if (getPlatform() == Platform.PURPUR || getPlatform() == Platform.PAPER) {
            return path.resolve("plugins");
        }

        return path.resolve("mods");
    }

    @Override
    public String getVersionName() {
        return version.getVersion();
    }

    @Override
    public Platform getPlatform() {
        return this.getVersion().getServerType().getPlatform();
    }

    @Override
    public @Nullable String getBuild() {
        return version.getTypeVersion();
    }

    @Override
    public boolean isServer() {
        return true;
    }

}
