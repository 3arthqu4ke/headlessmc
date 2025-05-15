package io.github.headlesshq.headlessmc.runtime.reflection;

import lombok.experimental.StandardException;
import io.github.headlesshq.headlessmc.api.command.CommandException;

@StandardException
public class SegmentationFault extends CommandException {
    public SegmentationFault(String message) {
        super(message);
    }

}
