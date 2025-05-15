package io.github.headlesshq.headlessmc.java;

import io.github.headlesshq.headlessmc.logging.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

@FunctionalInterface
public interface JavaScanner {
    JavaVersionParser getParser();

    default @Nullable Java scanJava(Logger log, String path) {
        log.debug("Reading Java version at path: " + path);
        if (path.trim().isEmpty()) {
            return null;
        }

        try {
            int majorVersion = getParser().parseVersionCommand(path);
            Java java = new Java(path.replace("\\", "/"), majorVersion);
            log.debug("Found Java: " + java);
            return java;
        } catch (IOException e) {
            log.warn("Couldn't parse Java Version for path " + path, e);
        }

        return null;
    }

    static JavaScanner of(JavaVersionParser parser) {
        return () -> parser;
    }

}
