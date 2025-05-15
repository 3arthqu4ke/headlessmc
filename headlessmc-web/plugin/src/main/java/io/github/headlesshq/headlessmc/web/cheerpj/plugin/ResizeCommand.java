package io.github.headlesshq.headlessmc.web.cheerpj.plugin;

import io.github.headlesshq.headlessmc.api.HeadlessMc;
import io.github.headlesshq.headlessmc.api.command.AbstractCommand;
import io.github.headlesshq.headlessmc.api.command.CommandException;

public class ResizeCommand extends AbstractCommand {
    private final CheerpJGUI gui;

    public ResizeCommand(HeadlessMc ctx, CheerpJGUI gui) {
        super(ctx, "resize", "Resizes the GUI window.");
        this.gui = gui;
    }

    @Override
    public void execute(String line, String... args) throws CommandException {
        if (args.length < 3) {
            throw new CommandException("Please specify width and height!");
        }

        try {
            int width = Integer.parseInt(args[1]);
            int height = Integer.parseInt(args[2]);
            ctx.log("Resizing to " + width + "x" + height);
            gui.scheduleSizeChange(width, height);
        } catch (NumberFormatException e) {
            throw new CommandException("Failed to parse " + args[1] + "x" + args[2] + "!");
        }
    }

}

