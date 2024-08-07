package me.earth.headlessmc.api.command.line;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.HeadlessMc;

import java.io.Console;
import java.io.IOError;

@CustomLog
@RequiredArgsConstructor
class ConsoleCommandLine implements CommandLine {
    private final Console console;

    @Override
    public void listen(HeadlessMc hmc) throws IOError {
        String line;
        CommandLineManager ctx = hmc.getCommandLineManager();
        while ((line = ctx.isHidingPasswords() ? String.valueOf(console.readPassword()) : console.readLine()) != null) {
            ctx.getCommandLineReader().accept(line);
            if (ctx.isQuickExitCli()) {
                if (!ctx.isWaitingForInput()) {
                    break;
                }

                log.debug("Waiting for more input...");
            }
        }
    }

}
