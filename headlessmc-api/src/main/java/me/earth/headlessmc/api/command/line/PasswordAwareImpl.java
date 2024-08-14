package me.earth.headlessmc.api.command.line;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.command.PasswordAware;

@Getter
@Setter
public class PasswordAwareImpl implements PasswordAware {
    private volatile boolean hidingPasswords;
    private volatile boolean hidingPasswordsSupported;

}
