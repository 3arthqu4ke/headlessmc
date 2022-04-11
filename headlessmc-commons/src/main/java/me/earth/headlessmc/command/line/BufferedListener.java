package me.earth.headlessmc.command.line;

import lombok.Cleanup;
import me.earth.headlessmc.api.command.HasCommandContext;

import java.io.*;

enum BufferedListener implements Listener {
    INSTANCE;

    @Override
    public void listen(HasCommandContext context) {
        try {
            @Cleanup
            BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(FileDescriptor.in)));

            String line;
            while ((line = in.readLine()) != null) {
                context.getCommandContext().execute(line);
            }
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

}
