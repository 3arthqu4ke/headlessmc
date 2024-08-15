package me.earth.headlessmc.web.cheerpj;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.command.AbstractLauncherCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FilesCommand extends AbstractLauncherCommand {
    public FilesCommand(Launcher ctx) {
        super(ctx, "files", "Lists files in directories.");
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (line.contains("..")) {
            throw new CommandException("Your path contains a '..' character, this could cause CheerpJ to crash!");
        }

        Path path = Paths.get("");
        if (args.length > 1) {
            try {
                path = Paths.get(args[1]);
            } catch (InvalidPathException e) {
                throw new CommandException(e.getMessage());
            }
        }

        if (!Files.exists(path)) {
            ctx.log("This file does not exist!");
            return;
        }

        if (CommandUtil.hasFlag("-delete", args)) {
            try {
                ctx.getFileManager().delete(path.toFile());
            } catch (IOException e) {
                ctx.log("Failed to delete " + path + ": " + e.getMessage());
            }

            return;
        }

        if (!Files.isDirectory(path)) {
            try {
                byte[] bytes = Files.readAllBytes(path);
                String content = new String(bytes);
                if (content.length() > 10_000 && !CommandUtil.hasFlag("-full", args)) {
                    ctx.log("File content has been limited to 10.000 characters, specify the -full flag if you want more.");
                    content = content.substring(0, 10_000);
                }

                ctx.log("Contents of file " + path);
                ctx.log(content);
            } catch (IOException e) {
                throw new CommandException(e.getMessage());
            }
        } else {
            try (Stream<Path> stream = Files.list(path)) {
                ctx.log(path.toAbsolutePath().toString());
                stream.forEach(file -> ctx.log("   " + file.getFileName().toString()));
            } catch (IOException e) {
                throw new CommandException(e.getMessage());
            }
        }
    }

}
