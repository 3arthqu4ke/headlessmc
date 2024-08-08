package me.earth.headlessmc.api.command.line;

import lombok.CustomLog;
import me.earth.headlessmc.api.HeadlessMc;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

@CustomLog
public class BufferedCommandLineListener implements CommandLineListener {
    @Override
    public void listen(HeadlessMc hmc) throws IOError {
        CommandLine ctx = hmc.getCommandLine();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(ctx.getInAndOutProvider().getIn().get()))) {
            String line;
            while ((line = in.readLine()) != null) {
                ctx.getCommandLineReader().accept(line);
                if (ctx.isQuickExitCli()) {
                    if (!ctx.isWaitingForInput()) {
                        break;
                    }

                    log.debug("Waiting for more input...");
                }
            }
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

}
