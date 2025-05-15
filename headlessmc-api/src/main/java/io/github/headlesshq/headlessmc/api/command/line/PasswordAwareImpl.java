package io.github.headlesshq.headlessmc.api.command.line;

import lombok.Getter;
import lombok.Setter;
import io.github.headlesshq.headlessmc.api.command.PasswordAware;

/**
 * Simplest implementation of {@link PasswordAware}.
 */
@Getter
@Setter
public class PasswordAwareImpl implements PasswordAware {
    private volatile boolean hidingPasswords;
    private volatile boolean hidingPasswordsSupported;

}
