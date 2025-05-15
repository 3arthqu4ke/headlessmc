package io.github.headlesshq.headlessmc.testplugin;

import lombok.CustomLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.Permission;

@CustomLog
@SuppressWarnings({"removal", "RedundantSuppression"})
public class ExitTrap {
    @Getter
    @RequiredArgsConstructor
    public static class ExitTrappedException extends SecurityException {
        private final int status;
    }

    public static void trapExit() {
        try {
            SecurityManager securityManager = new SecurityManager() {
                @Override
                public void checkPermission(Permission perm) {
                    if ("setSecurityManager".equals(perm.getName())) {
                        log.warn("Someone is setting a SecurityManager", new Exception("Stacktrace"));
                    }
                }

                @Override
                public void checkExit(int status) {
                    log.info("Preventing System.exit: " + status);
                    throw new ExitTrappedException(status);
                }
            };

            System.setSecurityManager(securityManager);
        } catch (Throwable throwable) {
            log.error("Failed to set SecurityManager", throwable);
        }
    }

    public static void remove() {
        try {
            System.setSecurityManager(null);
        } catch (Throwable throwable) {
            log.error("Failed to remove SecurityManager", throwable);
        }
    }

}
