package me.earth.headlessmc.api;

public interface PasswordAware {
    boolean isHidingPasswords();

    void setHidingPasswords(boolean hidingPasswords);

    default boolean isHidingPasswordsSupported() {
        return true;
    }

}
