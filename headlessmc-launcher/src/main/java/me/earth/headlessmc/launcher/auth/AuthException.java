package me.earth.headlessmc.launcher.auth;

import lombok.NoArgsConstructor;

// TODO: @StandardException
@NoArgsConstructor
public class AuthException extends Exception {
    public AuthException(String message) {
        super(message);
    }

}
