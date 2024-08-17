package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.HeadlessMc;

import java.util.List;

public class CopyContext extends CommandContextImpl {
    public CopyContext(HeadlessMc ctx, boolean baseContext) {
        super(ctx);
        CommandContext before = baseContext ? ctx.getCommandLine().getBaseContext() : ctx.getCommandLine().getCommandContext();
        for (Command command : before) {
            add(command);
        }
    }

    // TODO: actually we should always expose this
    @Override
    public void add(Command command) {
        super.add(command);
    }

    public List<Command> getCommandList() {
        return commands;
    }

}
