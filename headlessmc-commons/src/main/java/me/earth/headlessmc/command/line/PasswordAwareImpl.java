package me.earth.headlessmc.command.line;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.api.PasswordAware;

public class PasswordAwareImpl implements PasswordAware {
    @Getter
    @Setter
    private boolean hidingPasswords;

}
