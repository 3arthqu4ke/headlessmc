package me.earth.headlessmc.web.cheerpj.plugin;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.AbstractCommand;

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
