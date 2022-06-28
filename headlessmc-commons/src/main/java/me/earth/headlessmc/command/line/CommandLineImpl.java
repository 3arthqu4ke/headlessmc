package me.earth.headlessmc.command.line;

import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.earth.headlessmc.api.PasswordAware;
import me.earth.headlessmc.api.QuickExitCli;

@CustomLog
@RequiredArgsConstructor
public class CommandLineImpl extends PasswordAwareImpl implements Listener {
    private final PasswordAware ctx;

    public CommandLineImpl() {
        this.ctx = this;
    }

    @Override
    public void listen(QuickExitCli context) {
        val console = System.console();
        if (console == null) {
            log.error("Console is null, hiding passwords is not supported!");
            BufferedListener.INSTANCE.listen(context);
        } else {
            new ConsoleListener(ctx, console).listen(context);
        }
    }

    @Override
    public boolean isHidingPasswordsSupported() {
        return System.console() != null;
    }

}
