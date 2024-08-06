package me.earth.headlessmc.command.line;

import lombok.Cleanup;
import lombok.CustomLog;
import me.earth.headlessmc.api.QuickExitCli;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;

@CustomLog
enum BufferedListener implements Listener {
    INSTANCE;

    @Override
    public void listen(QuickExitCli context) {
        try {
            @Cleanup
            BufferedReader in = new BufferedReader(new InputStreamReader(context.getInAndOutProvider().getIn().get()));

            System.out.println(context.getInAndOutProvider().getIn().get());
            String line;
            while ((line = in.readLine()) != null) {
                log.info("Read line: " + line);
                System.out.println("Read line "+ line);
                System.out.flush();
                context.getCommandContext().execute(line);
                if (context.isQuickExitCli()) {
                    if (!context.isWaitingForInput()) {
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
