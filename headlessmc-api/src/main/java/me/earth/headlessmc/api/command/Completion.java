package me.earth.headlessmc.api.command;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

/**
 * A completion for a CommandLine that supports completion, like JLine.
 */
@Data
public class Completion implements HasDescription {
    private final String value;
    private final @Nullable String description;

}
