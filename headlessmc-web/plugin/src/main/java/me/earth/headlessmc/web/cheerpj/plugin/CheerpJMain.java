package me.earth.headlessmc.web.cheerpj.plugin;

import me.earth.headlessmc.api.process.InAndOutProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class CheerpJMain {
    public static final PrintStream STDOUT = System.out;

    public static void main(String[] args) {
        CheerpJGUI gui = CheerpJGUI.getInstance();
        gui.init();

        InAndOutProvider inAndOutProvider = new InAndOutProvider();
        PrintStream out = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // SwingUtilities.invokeLater(() -> gui.getDisplayArea().append(String.valueOf((char) b)));
                STDOUT.write(b);
            }

            @Override
            public void write(byte @NotNull [] b) throws IOException {
                STDOUT.write(b);
            }

            @Override
            public void write(byte @NotNull [] b, int off, int len) {
                STDOUT.write(b, off, len);
            }
        }, true);

        System.setOut(out);
        System.setErr(out);
        Thread setOutThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(250);
                    System.setOut(out);
                    System.setErr(out);
                } catch (InterruptedException e) {
                    e.printStackTrace(out);
                }
            }
        });

        setOutThread.start();

        inAndOutProvider.setConsole(() -> null);
        inAndOutProvider.setOut(() -> out);
        inAndOutProvider.setErr(() -> out);
        inAndOutProvider.setIn(() -> new InputStream() {
            @Override
            public int read() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        new CheerpJLauncher(inAndOutProvider, gui).launch();
    }

}
