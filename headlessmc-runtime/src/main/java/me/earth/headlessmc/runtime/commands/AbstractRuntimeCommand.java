package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.command.AbstractCommand;
import me.earth.headlessmc.runtime.Runtime;

public abstract class AbstractRuntimeCommand extends AbstractCommand {
    protected final Runtime ctx;

    public AbstractRuntimeCommand(Runtime ctx, String name, String desc) {
        super(ctx, name, desc);
        this.ctx = ctx;
    }

}
