package me.earth.headlessmc.runtime.commands.reflection;

import me.earth.headlessmc.api.command.AbstractCommand;
import me.earth.headlessmc.runtime.reflection.RuntimeReflection;

public abstract class AbstractRuntimeCommand extends AbstractCommand {
    public final RuntimeReflection ctx;

    public AbstractRuntimeCommand(RuntimeReflection ctx, String name, String description) {
        super(ctx, name, description);
        this.ctx = ctx;
    }

}
