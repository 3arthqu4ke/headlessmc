package me.earth.headlessmc.runtime;

import lombok.experimental.StandardException;
import me.earth.headlessmc.api.command.CommandException;

@StandardException
public class SegmentationFault extends CommandException {
    public SegmentationFault(String message) {
        super(message);
    }

}
