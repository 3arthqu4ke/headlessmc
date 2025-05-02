package me.earth.headlessmc.launcher.server.downloader;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.command.download.ModLauncherCommand;
import me.earth.headlessmc.launcher.server.ServerTypeDownloader;
import me.earth.headlessmc.launcher.version.Version;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ModLauncherCommandDownloader implements ServerTypeDownloader.DownloadHandler {
    private final Version version;
    private final String build;
    private final String name;
    private final @Nullable String typeVersion;
    private final String[] args;
    private final ModLauncherCommand command;

    @Override
    public Path download(ServerTypeDownloader.TypeVersionToDownloadPathResolve pathResolver) throws IOException {
        Path directory = pathResolver.resolve(build);

        List<String> commandArgs = new ArrayList<>();
        commandArgs.add(name);
        commandArgs.add(version.getName());
        commandArgs.add("-server");
        commandArgs.add("--dir");
        commandArgs.add(directory.toAbsolutePath().toString());
        if (typeVersion != null) {
            commandArgs.add("--uid");
            commandArgs.add(typeVersion);
        }

        commandArgs.addAll(Arrays.stream(args)
                .filter(arg -> !"-list".equalsIgnoreCase(arg))
                .collect(Collectors.toList()));

        try {
            Files.createDirectories(directory);
            command.execute(version, commandArgs.toArray(new String[0]));
        } catch (CommandException e) {
            throw new IOException(e);
        }

        // we also need the server.jar for the forge server
        // vanillaDownloader.download(launcher, version, null).download(ignored -> directory);
        return directory;
    }

}
