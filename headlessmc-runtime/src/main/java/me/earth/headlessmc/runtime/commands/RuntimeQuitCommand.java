package me.earth.headlessmc.runtime.commands;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.CommandUtil;
import me.earth.headlessmc.api.command.impl.QuitCommand;
import me.earth.headlessmc.api.command.YesNoContext;

public class RuntimeQuitCommand extends QuitCommand {
    public RuntimeQuitCommand(HeadlessMc ctx) {
        super(ctx);
        args.put("-y", "Will not ask you if you want to quit.");
    }

    @Override
    public void execute(String line, String... args) {
        if (CommandUtil.hasFlag("-y", args)) {
            super.execute(line, args);
        } else {
            ctx.log("Minecraft won't save properly. Quit anyways (Y/N)?");
            YesNoContext.goBackAfter(ctx, result -> {
                if (result) {
                    super.execute(line, args);
                }
            });
        }
    }

    @Override
    public String getDescription() {
        return "Quits the game.";
    }

}
