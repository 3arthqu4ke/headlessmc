package me.earth.headlessmc.cheerpj;

import lombok.AccessLevel;
import lombok.Getter;
import me.earth.headlessmc.api.command.PasswordAware;
import me.earth.headlessmc.api.util.Lazy;
import me.earth.headlessmc.launcher.Launcher;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Getter
public class CheerpJGUI implements PasswordAware {
    private final JFrame frame = new JFrame("HeadlessMc - " + Launcher.VERSION);

    private final JPanel panel = new JPanel();
    private final JTextArea displayArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(displayArea);
    private final JPasswordField inputField = new JPasswordField();
    private final JPanel inputPanel = new JPanel();
    private final Lazy<Consumer<String>> commandHandler =
            new Lazy<>(() -> (str -> displayArea.append("HeadlessMc is still initializing, command ignored.\n")), null);

    @Getter(AccessLevel.NONE)
    private final AtomicBoolean hidingPasswords = new AtomicBoolean(false);

    public void init() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Font monospacedFont = new Font("Monospaced", Font.PLAIN, 12);
        displayArea.setFont(monospacedFont);
        inputField.setFont(monospacedFont);

        displayArea.setEditable(false);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        inputField.setEchoChar((char) 0);
        inputField.addActionListener(e -> {
            char[] inputText = inputField.getPassword();
            String textString = new String(inputText);
            Arrays.fill(inputText, (char) 0);
            if (inputField.getEchoChar() != '*') {
                displayArea.append(textString + "\n");
            }

            commandHandler.get().accept(textString);
            inputField.setText("");
        });

        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        inputPanel.add(inputField, BorderLayout.CENTER);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public boolean isHidingPasswords() {
        return hidingPasswords.get();
    }

    @Override
    public void setHidingPasswords(boolean hidingPasswords) {
        synchronized (this) {
            this.hidingPasswords.set(hidingPasswords);
            SwingUtilities.invokeLater(() -> {
                if (hidingPasswords) {
                    inputField.setEchoChar('*');
                } else {
                    inputField.setEchoChar((char) 0);
                }
            });
        }
    }

    @Override
    public boolean isHidingPasswordsSupported() {
        return true;
    }

    @Override
    public void setHidingPasswordsSupported(boolean hidingPasswordsSupported) {
        // NOP
    }

}
