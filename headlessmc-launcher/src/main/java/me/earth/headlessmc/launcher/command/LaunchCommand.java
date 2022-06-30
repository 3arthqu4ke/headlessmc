package me.earth.headlessmc.launcher.command;

import lombok.CustomLog;
import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.files.FileUtil;
import me.earth.headlessmc.launcher.launch.LaunchException;
import me.earth.headlessmc.launcher.version.Version;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@CustomLog
public class LaunchCommand extends AbstractVersionCommand {
    public LaunchCommand(Launcher launcher) {
        super(launcher, "launch", "Launches the game.");
        args.put("<version/id>", "Name or id of the version to launch." +
            " If you use the id you need to use the -id flag as well.");
        args.put("-id",
                 "Use if you specified an id instead of a version name.");
        args.put("-commands",
                 "Starts the game with the built-in command line support.");
        args.put("-lwjgl", "Removes lwjgl code, causing Minecraft" +
            " not to render anything.");
        args.put("-jndi", "Patches the Log4J vulnerability.");
        args.put("-lookup", "Patches the Log4J vulnerability even harder.");
        args.put("-paulscode", "Removes some error messages from the" +
            " PaulsCode library which may annoy you if you started the" +
            " game with the -lwjgl flag.");
        args.put("-noout", "Doesn't print Minecrafts output to the console.");
    }

    @Override
    public void execute(Version version, String... args)
        throws CommandException {
        val uuid = UUID.randomUUID();
        ctx.log("Launching version " + version.getName() + ", " + uuid);
        val files = ctx.getFileManager().createRelative(uuid.toString());
        val shutdownHook = new Thread(() -> {
            try {
                FileUtil.delete(files.getBase());
            } catch (IOException e) {
                log.error("Couldn't delete files of game "
                              + files.getBase().getName()
                              + ": " + e.getMessage());
            }
        });

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        try {
            val process = ctx.getProcessFactory().run(
                version, ctx, files,
                CommandUtil.hasFlag("-commands", args),
                CommandUtil.hasFlag("-lwjgl", args),
                CommandUtil.hasFlag("-jndi", args),
                CommandUtil.hasFlag("-lookup", args),
                CommandUtil.hasFlag("-paulscode", args),
                CommandUtil.hasFlag("-noout", args));
            try {
                int status = process.waitFor();
                ctx.log("Minecraft exited with code: " + status);
            } catch (InterruptedException ie) {
                ctx.log("Launcher has been interrupted...");
                Thread.currentThread().interrupt();
            }
        } catch (IOException | LaunchException | AuthException e) {
            e.printStackTrace();
            throw new CommandException(String.format(
                "Couldn't launch %s: %s", version.getName(), e.getMessage()));
        } finally {
            if (!CommandUtil.hasFlag("-keep", args)) {
                log.debug("Deleting " + files.getBase().getAbsolutePath());
                //noinspection CallToThreadRun
                shutdownHook.run();
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            }

            if (!CommandUtil.hasFlag("-exit", args)) {
                System.exit(0);
            }
        }
    }

}
