package io.github.headlesshq.headlessmc.api.command;

public interface PasswordAware {
    boolean isHidingPasswords();

    void setHidingPasswords(boolean hidingPasswords);

    boolean isHidingPasswordsSupported();

    void setHidingPasswordsSupported(boolean hidingPasswordsSupported);

}
