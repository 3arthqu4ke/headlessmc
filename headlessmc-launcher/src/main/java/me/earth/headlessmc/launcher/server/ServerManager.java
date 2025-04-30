package me.earth.headlessmc.launcher.server;

import lombok.Cleanup;
import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LazyService;
import me.earth.headlessmc.launcher.command.download.VersionInfoCache;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.modlauncher.Modlauncher;
import me.earth.headlessmc.launcher.server.downloader.FabricDownloader;
import me.earth.headlessmc.launcher.server.downloader.ForgeDownloader;
import me.earth.headlessmc.launcher.server.downloader.PaperDownloader;
import me.earth.headlessmc.launcher.server.downloader.VanillaDownloader;
import me.earth.headlessmc.launcher.version.VersionService;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Getter
@CustomLog
@RequiredArgsConstructor
public class ServerManager extends LazyService<Server> {
    private final List<ServerType> serverTypes = new ArrayList<>();
    private final Path serversDir;

    @Override
    protected Collection<Server> update() {
        List<Server> servers = new ArrayList<>();
        for (ServerType type : serverTypes) {
            Path typeDir = serversDir.resolve(type.getName());
            if (!Files.exists(typeDir)) {
                continue;
            }

            try (Stream<Path> files = Files.list(typeDir)) {
                for (Path mcVersion : itr(files.filter(Files::isDirectory))) {
                    @Cleanup
                    Stream<Path> builds = Files.list(mcVersion);
                    for (Path build : itr(builds.filter(Files::isDirectory))) {
                        @Cleanup
                        Stream<Path> serverDirs = Files.list(build);
                        for (Path serverDir : itr(serverDirs.filter(Files::isDirectory))) {
                            servers.add(new Server(
                                serverDir,
                                serverDir.getFileName().toString(),
                                new ServerVersion(
                                        type,
                                        mcVersion.getFileName().toString(),
                                        build.getFileName().toString()
                                ),
                                servers.size()
                            ));
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Failed to read files of " + type, e);
            }
        }

        return servers;
    }

    public @Nullable Server getServer(String serverName) {
        return getContents()
                .stream()
                .filter(s -> s.getName().equalsIgnoreCase(serverName))
                .findFirst()
                .orElse(null);
    }

    public Path add(Launcher launcher,
                    ServerType type,
                    @Nullable String nameIn,
                    @Nullable String versionIn,
                    @Nullable String typeVersionIn,
                    String... args) throws IOException {
        String version;
        if (versionIn == null) {
            version = launcher
                    .getVersionInfoCache()
                    .cache(false)
                    .stream()
                    .filter(v -> "release".equalsIgnoreCase(v.getType()))
                    .findFirst()
                    .orElseThrow(() -> new IOException("Failed to find any Minecraft versions!"))
                    .getName();
        } else {
            version = versionIn;
        }

        launcher.log("Adding " + type.getName() + " server for " + version);
        ServerTypeDownloader.DownloadHandler downloadHandler =
                type.getDownloader().download(launcher, version, typeVersionIn, args);
        Path result = downloadHandler.download(typeVersion -> {
            String name = nameIn;
            if (name == null) {
                name = type.getName() + "-" + version + (typeVersion == null ? "" : "-" + typeVersion);
                int i = 1;
                while (getServer(name) != null) {
                    name = type.getName() + "-" + version + (typeVersion == null ? "" : "-" + typeVersion) + i++;
                }
            }

            Server server = getServer(name);
            if (server != null) {
                throw new IOException(String.format("Server %s already exists", name));
            }

            return serversDir
                    .resolve(type.getName())
                    .resolve(version)
                    .resolve(typeVersion == null ? "latest" : typeVersion)
                    .resolve(name);
        });

        refresh();
        return result;
    }

    public void remove(Server server) throws IOException {
        FileManager fileManager = new FileManager(server.getPath().toAbsolutePath().toString());
        fileManager.delete(server.getPath().toFile());
        contents.remove(server);
        refresh();
    }

    public @Nullable ServerType getServerType(String name) {
        for (ServerType type : serverTypes) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }

        return null;
    }

    public static ServerManager create(FileManager launcherFileManager) {
        Path serversDir = launcherFileManager.createRelative("servers").getBase().toPath();
        ServerManager serverManager = new ServerManager(serversDir);
        serverManager.getServerTypes().add(new ServerType("paper", new PaperDownloader()));
        serverManager.getServerTypes().add(new ServerType("fabric", new FabricDownloader(new VanillaDownloader())));
        serverManager.getServerTypes().add(new ServerType("vanilla", new VanillaDownloader()));
        serverManager.getServerTypes().add(new ServerType("neoforge", new ForgeDownloader(new VanillaDownloader(), Modlauncher.NEOFORGE)));
        serverManager.getServerTypes().add(new ServerType("forge", new ForgeDownloader(new VanillaDownloader(), Modlauncher.LEXFORGE)));
        return serverManager;
    }

    private <T> Iterable<T> itr(Stream<T> stream) {
        return stream::iterator;
    }

}
