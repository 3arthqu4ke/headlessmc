package me.earth.headlessmc.launcher.os;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.headlessmc.api.HasName;

/**
 * Represents an Operating System.
 */
@Data
public class OS implements HasName {
    private final String name;
    private final Type type;
    private final String version;
    private final boolean arch;

    @Getter
    @RequiredArgsConstructor
    public enum Type implements HasName {
        WINDOWS("windows"),
        LINUX("linux"),
        OSX("osx"),
        UNKNOWN("unknown");

        private final String name;
    }

}
