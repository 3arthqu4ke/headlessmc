package me.earth.headlessmc.launcher.server;

import lombok.Data;
import me.earth.headlessmc.api.HasId;
import me.earth.headlessmc.api.HasName;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;

@Data
public class Server implements HasName, HasId {
    private final Path path;
    private final String name;
    private final ServerVersion version;
    private final int id;

    public Path getEula() {
        return path.resolve("eula.txt");
    }

    public boolean hasEula() {
        return Files.exists(getEula());
    }

    public Path getJar() {
        return path.resolve("server.jar");
    }

    public boolean hasCustomName() {
        return !name.startsWith(getName(version.getServerType(), version.getVersion(), version.getTypeVersion()));
    }

    public static String getName(ServerType type, String version, @Nullable String typeVersion) {
        return type.getName() + "-" + version + (version == null ? "" : "-" + typeVersion);
    }

}
