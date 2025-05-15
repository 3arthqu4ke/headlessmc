package io.github.headlesshq.headlessmc.runtime.commands.reflection;

import io.github.headlesshq.headlessmc.api.command.AbstractCommand;
import io.github.headlesshq.headlessmc.runtime.reflection.RuntimeReflection;

public abstract class AbstractRuntimeCommand extends AbstractCommand {
    public final RuntimeReflection ctx;

    public AbstractRuntimeCommand(RuntimeReflection ctx, String name, String description) {
        super(ctx, name, description);
        this.ctx = ctx;
    }

}
