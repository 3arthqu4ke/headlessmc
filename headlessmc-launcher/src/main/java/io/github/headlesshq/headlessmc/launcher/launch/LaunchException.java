package io.github.headlesshq.headlessmc.launcher.launch;

import lombok.experimental.StandardException;

@StandardException
public class LaunchException extends Exception {
    public LaunchException(String message) {
        super(message);
    }

}
