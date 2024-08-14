package me.earth.headlessmc.cheerpj;

import lombok.extern.slf4j.Slf4j;
import me.earth.headlessmc.api.process.InAndOutProvider;

import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Main {
    public static void main(String[] args) {
        if (true) {
            try {
                System.out.println(KeyFactory.getInstance("EC").getClass().getName());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        CheerpJGUI gui = new CheerpJGUI();
        gui.init();

        InAndOutProvider inAndOutProvider = new InAndOutProvider();
        PrintStream out = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> gui.getDisplayArea().append(String.valueOf((char) b)));
            }
        }, true);

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
