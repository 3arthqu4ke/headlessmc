package me.earth.headlessmc.command.line;

import lombok.*;
import me.earth.headlessmc.api.PasswordAware;
import me.earth.headlessmc.api.QuickExitCli;

@CustomLog
@RequiredArgsConstructor
public class CommandLineImpl implements Listener, PasswordAware {
    @Getter
    @Setter
    private boolean hidingPasswords;
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
