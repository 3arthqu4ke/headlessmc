package me.earth.headlessmc.command.line;

import lombok.Cleanup;
import me.earth.headlessmc.api.QuickExitCli;

import java.io.*;

enum BufferedListener implements Listener {
    INSTANCE;

    @Override
    public void listen(QuickExitCli context) {
        try {
            @Cleanup
            BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(FileDescriptor.in)));

            String line;
            while ((line = in.readLine()) != null) {
                context.getCommandContext().execute(line);
                if (context.isQuickExitCli() && !context.isWaitingForInput()) {
                    return;
                }
            }
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

}
