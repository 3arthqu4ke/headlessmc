package me.earth.headlessmc.api.command.line;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.HeadlessMc;

import java.io.Console;
import java.io.IOError;

@CustomLog
@RequiredArgsConstructor
class ConsoleCommandLineReader implements CommandLineReader {
    private final Console console;

    @Override
    public void read(HeadlessMc hmc) throws IOError {
        String line;
        CommandLine ctx = hmc.getCommandLine();
        while ((line = ctx.isHidingPasswords() ? String.valueOf(console.readPassword()) : console.readLine()) != null) {
            ctx.getCommandConsumer().accept(line);
            if (ctx.isQuickExitCli()) {
                if (!ctx.isWaitingForInput()) {
                    break;
                }

                log.debug("Waiting for more input...");
            }
        }
    }

}
