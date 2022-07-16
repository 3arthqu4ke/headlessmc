package me.earth.headlessmc.launcher;

import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.command.line.Listener;

/**
 * @see me.earth.headlessmc.api.QuickExitCli
 */
@UtilityClass
public class QuickExitCliHandler {
    /**
     * Checks if the args contain a "--command". If they do the rest of the args
     * will get collected into a command. If the args contain no "--command",
     * the command is empty or "cli" <tt>false</tt> will be returned. Otherwise
     * <tt>true</tt> will be returned, the given {@link Launcher}s command
     * context will be run on the given command and
     * {@link Launcher#setQuickExitCli(boolean)} will be set to
     * <tt>true</tt>. If a command needs more input it can set
     * {@link Launcher#setWaitingForInput(boolean)} to <tt>true</tt> which will
     * cause this method call the given {@link Listener}. An exception are the
     * args "--version", "-version" or "version". These will also return
     * <tt>true</tt> and print {@link Launcher#VERSION}.
     *
     *
     * @param launcher the launcher.
     * @param in the Listener to call when a command waits for input.
     * @param args the arguments to check.
     * @return <tt>true</tt> if the launcher shouldn't listen to more commands.
     */
    public static boolean checkQuickExit(Launcher launcher, Listener in,
                                         String... args) {
        val cmd = collectArgs(launcher, args);
        if (cmd != null) {
            if (cmd.isEmpty() || "cli".equalsIgnoreCase(cmd)) {
                return false;
            }

            if (isVersion(cmd)) {
                launcher.log("HeadlessMc - " + Launcher.VERSION);
                return true;
            }

            launcher.setQuickExitCli(true);
            launcher.getCommandContext().execute(cmd);
            if (launcher.isWaitingForInput()) {
                in.listen(launcher);
            }
        }

        return cmd != null;
    }

    private static String collectArgs(Launcher launcher, String... args) {
        var quickExitCli = false;
        val cmd = new StringBuilder();
        for (val arg : args) {
            if (arg == null) {
                continue;
            }

            if (!quickExitCli && isVersion(arg)) {
                launcher.log("HeadlessMc - " + Launcher.VERSION);
                return "--version";
            }

            if (quickExitCli) {
                cmd.append(arg).append(" ");
            }

            if (arg.equalsIgnoreCase("--command")) {
                quickExitCli = true;
            }
        }

        return quickExitCli ? cmd.toString().trim() : null;
    }

    private static boolean isVersion(String string) {
        return string.equalsIgnoreCase("--version")
            || string.equalsIgnoreCase("-version")
            || string.equalsIgnoreCase("version");
    }

}
