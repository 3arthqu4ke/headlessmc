package me.earth.headlessmc.launcher.command;

import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.config.Property;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
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
        // TODO: is this really necessary?
        args.put("-noout", "Doesn't print Minecrafts output to the console.");
        args.put("-quit", "Quit HeadlessMc after launching the game.");
    }

    @Override
    public void execute(Version version, String... args)
        throws CommandException {
        val uuid = UUID.randomUUID();
        ctx.log("Launching version " + version.getName() + ", " + uuid);
        val files = ctx.getFileManager().createRelative(uuid.toString());

        boolean quit = flag("-quit", LauncherProperties.INVERT_QUIT_FLAG, args);
        try {
            val process = ctx.getProcessFactory().run(version, ctx, files,
                CommandUtil.hasFlag("-commands", args),
                flag("-lwjgl", LauncherProperties.INVERT_LWJGL_FLAG, args),
                flag("-jndi", LauncherProperties.INVERT_JNDI_FLAG, args),
                CommandUtil.hasFlag("-lookup", args),
                flag("-paulscode", LauncherProperties.INVERT_PAULS_FLAG, args),
                quit || CommandUtil.hasFlag("-noout", args), quit);

            if (quit) {
                System.exit(0);
            }

            try {
                int status = process.waitFor();
                ctx.log("Minecraft exited with code: " + status);
            } catch (InterruptedException ie) {
                ctx.log("Launcher has been interrupted...");
                Thread.currentThread().interrupt();
            }
        } catch (IOException | LaunchException | AuthException e) {
            e.printStackTrace();
            ctx.log(String.format(
                "Couldn't launch %s: %s", version.getName(), e.getMessage()));
        } catch (Throwable t) {
            val msg = String.format(
                "Couldn't launch %s: %s", version.getName(), t.getMessage());
            log.error(msg);
            ctx.log(msg);
            t.printStackTrace();
            throw t;
        } finally {
            // for some reason both ShutdownHooks and File.deleteOnExit are
            // not really working, that's why we Main.deleteOldFiles, too.
            if (!CommandUtil.hasFlag("-keep", args)
                && !ctx.getConfig().get(LauncherProperties.KEEP_FILES, false)) {
                try {
                    log.info("Deleting " + files.getBase().getName());
                    FileUtil.delete(files.getBase());
                } catch (IOException e) {
                    log.error("Couldn't delete files of game "
                                  + files.getBase().getName()
                                  + ": " + e.getMessage());
                }
            }

            if (!CommandUtil.hasFlag("-stay", args)) {
                System.exit(0);
            }
        }
    }

    private boolean flag(String flg, Property<Boolean> inv, String... args) {
        return CommandUtil.hasFlag(flg, args) ^ ctx.getConfig().get(inv, false);
    }

}
