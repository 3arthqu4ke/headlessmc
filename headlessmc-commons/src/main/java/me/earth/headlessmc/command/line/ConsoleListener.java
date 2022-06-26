package me.earth.headlessmc.command.line;

import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.PasswordAware;
import me.earth.headlessmc.api.QuickExitCli;

import java.io.Console;

@RequiredArgsConstructor
class ConsoleListener implements Listener {
    private final PasswordAware ctx;
    private final Console console;

    @Override
    public void listen(QuickExitCli context) {
        String line;
        while ((line = ctx.isHidingPasswords()
            ? String.valueOf(console.readPassword())
            : console.readLine()) != null) {
            context.getCommandContext().execute(line);
            if (context.isQuickExitCli() && !context.isWaitingForInput()) {
                return;
            }
        }
    }

}
