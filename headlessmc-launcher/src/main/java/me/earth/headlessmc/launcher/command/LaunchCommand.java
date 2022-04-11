package me.earth.headlessmc.launcher.command;

import lombok.CustomLog;
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

@CustomLog
public class LaunchCommand extends AbstractVersionCommand {
    public LaunchCommand(Launcher launcher) {
        super(launcher, "launch", "Launches the game.");
    }

    @Override
    public void execute(Version version, String... args)
        throws CommandException {
        UUID uuid = UUID.randomUUID();
        ctx.log("Launching version " + version.getName() + ", " + uuid);
        val files = ctx.getFileManager().createRelative(uuid.toString());

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
                try {
                    log.debug("Deleting " + files.getBase().getAbsolutePath());
                    FileUtil.delete(files.getBase());
                } catch (IOException ioe) {
                    log.error("Couldn't delete files of game "
                                + files.getBase().getName()
                                + ": " + ioe.getMessage());
                    ioe.printStackTrace();
                }
            }

            if (CommandUtil.hasFlag("-exit", args)) {
                System.exit(0);
            }
        }
    }

}
