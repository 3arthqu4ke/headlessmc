package me.earth.headlessmc.launcher;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.auth.AbstractLoginCommand;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.version.VersionUtil;

/**
 * Main entry point for HeadlessMc.
 */
@CustomLog
@UtilityClass
public final class Main {
    public static void main(String[] args) {
        ExitManager exitManager = new ExitManager();
        Throwable throwable = null;
        try {
            runHeadlessMc(exitManager, args);
        } catch (Throwable t) {
            throwable = t;
        } finally {
            exitManager.onMainThreadEnd(throwable);
            // These "System.exit()" calls are here because of the LoginCommands
            // -webview option. It seems that after closing the JFrame there is
            // still either the AWT, Webview or Javafx thread running, keeping the
            // program alive.
            try {
                if (throwable == null) {
                    exitManager.exit(0);
                } else {
                    log.error(throwable);
                    exitManager.exit(-1);
                }
            } catch (Throwable exitThrowable) {
                // it is possible, if we launch in memory, that forge prevents us from calling System.exit through their SecurityManager
                if (throwable != null && exitThrowable.getClass() == throwable.getClass()) { // we have logged FMLSecurityManager$ExitTrappedException before
                    log.error("Failed to exit!", exitThrowable);
                }

                // TODO: exit gracefully, try to call Forge to exit
            }
        }
    }

    private static void runHeadlessMc(ExitManager exitManager, String... args) throws AuthException {
        LauncherBuilder builder = new LauncherBuilder();
        builder.exitManager(exitManager);
        builder.initLogging();
        AbstractLoginCommand.replaceLogger();
        if (Main.class.getClassLoader() == ClassLoader.getSystemClassLoader()) {
            log.warn("You are not running from headlessmc-launcher-wrapper. Some things will not work properly!");
        }

        Launcher launcher = builder.buildDefault();
        if (!QuickExitCliHandler.checkQuickExit(launcher, args)) {
            log.info(String.format("Detected: %s", builder.os()));
            log.info(String.format("Minecraft Dir: %s", launcher.getMcFiles()));
            launcher.log(VersionUtil.makeTable(VersionUtil.releases(launcher.getVersionService().getContents())));
            launcher.getCommandLine().read(launcher.getHeadlessMc());
        }
    }

}
