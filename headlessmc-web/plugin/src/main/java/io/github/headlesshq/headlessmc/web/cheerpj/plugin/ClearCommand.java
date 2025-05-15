package io.github.headlesshq.headlessmc.web.cheerpj.plugin;

import io.github.headlesshq.headlessmc.api.HeadlessMc;
import io.github.headlesshq.headlessmc.api.command.AbstractCommand;

import javax.swing.*;

public class ClearCommand extends AbstractCommand {
    private final CheerpJGUI gui;

    public ClearCommand(HeadlessMc ctx, CheerpJGUI gui) {
        super(ctx, "clear", "Clears the GUI.");
        this.gui = gui;
    }

    @Override
    public void execute(String line, String... args) {
        SwingUtilities.invokeLater(() -> gui.getDisplayArea().setText(""));
    }

}
