package me.earth.headlessmc.launcher.command;

import lombok.CustomLog;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.api.command.ParseUtil;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherProperties;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.auth.LaunchAccount;
import me.earth.headlessmc.launcher.auth.ValidatedAccount;
import me.earth.headlessmc.launcher.command.download.AbstractDownloadingVersionCommand;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.launch.LaunchException;
import me.earth.headlessmc.launcher.launch.LaunchOptions;
import me.earth.headlessmc.launcher.version.Version;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

import static me.earth.headlessmc.api.command.CommandUtil.flag;
import static me.earth.headlessmc.launcher.LauncherProperties.RE_THROW_LAUNCH_EXCEPTIONS;

@CustomLog
public class LaunchCommand extends AbstractDownloadingVersionCommand {
    public LaunchCommand(Launcher launcher) {
        super(launcher, "launch", "Launches the game.");
        args.put("<version/id>", "Name or id of the version to launch. If you use the id you need to use the -id flag as well.");
        args.put("-id", "Use if you specified an id instead of a version name.");
        args.put("-commands", "Starts the game with the built-in command line support.");
        args.put("-lwjgl", "Removes lwjgl code, causing Minecraft not to render anything.");
        args.put("-inmemory", "Launches the game in the same JVM headlessmc is running in.");
        args.put("-jndi", "Patches the Log4J vulnerability.");
        args.put("-lookup", "Patches the Log4J vulnerability even harder.");
        args.put("-paulscode", "Removes some error messages from the PaulsCode library which may annoy you if you started the game with the -lwjgl flag.");
        args.put("-noout", "Doesn't print Minecrafts output to the console."); // TODO: is this really necessary?
        args.put("-quit", "Quit HeadlessMc after launching the game.");
        args.put("--jvm", "Jvm args to use.");
        args.put("--retries", "The amount of times you want to retry running Minecraft.");
    }

    @Override
    public void execute(Version version, String... args) throws CommandException {
        boolean prepare = CommandUtil.hasFlag("-prepare", args);
        val uuid = UUID.fromString(ctx.getConfig().get(LauncherProperties.EXTRACTED_FILE_CACHE_UUID, UUID.randomUUID().toString()));
        ctx.log((prepare ? "Preparing" : "Launching") + " version " + version.getName() + ", " + uuid);
        ctx.getLoggingService().setLevel(Level.INFO, true);
        val files = ctx.getFileManager().createRelative(uuid.toString());

        boolean quit = flag(ctx, "-quit", LauncherProperties.INVERT_QUIT_FLAG, LauncherProperties.ALWAYS_QUIT_FLAG, args);
        int status = 0;
        try {
            status = runProcess(version, files, quit, prepare, args);
        } catch (LaunchException | AuthException e) {
            status = -1;
            // ignore this specific message for tests, because otherwise Intellij shows it as red which looks like something failed
            if (!(e instanceof LaunchException && "Mock Factory".equals(e.getMessage()))) {
                log.error(e);
            }

            ctx.log(String.format("Couldn't launch %s: %s", version.getName(), e.getMessage()));
            if (ctx.getConfig().get(RE_THROW_LAUNCH_EXCEPTIONS, false)) {
                throw new IllegalStateException(e);
            }
        } catch (Throwable t) {
            status = -1;
            val msg = String.format("Couldn't launch %s: %s", version.getName(), t.getMessage());
            log.error(msg, t);
            ctx.log(msg);
            throw t;
        } finally {
            cleanup(files, args);
            if (!prepare && !CommandUtil.hasFlag("-stay", args)) {
                ctx.getExitManager().exit(status);
            }
        }

        if (status != 0 && ctx.getConfig().get(RE_THROW_LAUNCH_EXCEPTIONS, false)) {
            throw new IllegalStateException("Minecraft exited with code " + status);
        }

        if (!prepare) {
            try {
                ctx.getCommandLine().open(ctx);
            } catch (IOException ioException) {
                throw new IllegalStateException("Failed to reopen HeadlessMc CommandLineReader", ioException);
            }
        }
    }

    private int runProcess(Version version, FileManager files, boolean quit, boolean prepare, String... args)
            throws CommandException, LaunchException, AuthException {
        int status = 0;
        LaunchAccount account = getAccount();
        String retriesOption = CommandUtil.getOption("--retries", args);
        int retries = 0;
        if (retriesOption != null) {
            retries = ParseUtil.parseI(retriesOption);
        }

        Throwable throwable = null;
        for (int i = 0; i < retries + 1; i++) {
            if (i > 0) {
                log.warn("Retrying to launch Minecraft: " + i);
            }

            try {
                Process process = ctx.getProcessFactory().run(
                        LaunchOptions.builder()
                                .account(account)
                                .version(version)
                                .launcher(ctx)
                                .files(files)
                                .closeCommandLine(!prepare)
                                .parseFlags(ctx, quit, args)
                                .prepare(prepare)
                                .build()
                );

                if (prepare) {
                    return 0;
                }

                if (process == null) {
                    ctx.log("InMemory main thread ended.");
                }

                if (quit || process == null) {
                    cleanup(files, args);
                    ctx.getExitManager().exit(0);
                    return 0;
                }

                try {
                    status = process.waitFor();
                    ctx.log("Minecraft exited with code: " + status);
                } catch (InterruptedException ie) {
                    ctx.log("Launcher has been interrupted...");
                    Thread.currentThread().interrupt();
                }

                if (status == 0) {
                    break;
                }
            } catch (Throwable t) {
                status = -1;
                log.error("Failed to start Minecraft on try " + i, t);
                if (throwable == null) {
                    throwable = t;
                } else {
                    throwable.addSuppressed(t);
                }
            }
        }

        if (status != 0 && throwable != null) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }

            throw new LaunchException(throwable);
        }

        return status;
    }

    private void cleanup(FileManager files, String... args) {
        // for some reason both ShutdownHooks and File.deleteOnExit are
        // not really working, that's why we Main.deleteOldFiles, too.
        if (!CommandUtil.hasFlag("-keep", args) && !ctx.getConfig().get(LauncherProperties.KEEP_FILES, false)) {
            try {
                log.info("Deleting " + files.getBase().getName());
                ctx.getFileManager().delete(files.getBase());
            } catch (IOException e) {
                log.error("Couldn't delete files of game " + files.getBase().getName(), e);
            }
        }
    }

    protected LaunchAccount getAccount() throws CommandException {
        try {
            ValidatedAccount account = ctx.getAccountManager().getPrimaryAccount();
            if (account == null) {
                if (ctx.getAccountManager().getOfflineChecker().isOffline()) {
                    return ctx.getAccountManager().getOfflineAccount(ctx.getConfig());
                }

                throw new AuthException("You can't play the game without an account! Please use the login command.");
            } else {
                account = ctx.getAccountManager().refreshAccount(account);
                return account.toLaunchAccount();
            }

        } catch (AuthException e) {
            throw new CommandException(e.getMessage());
        }
    }

}
