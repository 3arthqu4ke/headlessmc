package me.earth.headlessmc.web.cheerpj.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import me.earth.headlessmc.api.command.PasswordAware;
import me.earth.headlessmc.web.cheerpj.Resizer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@Getter
public class CheerpJGUI implements PasswordAware {
    @Getter
    private static final CheerpJGUI instance = new CheerpJGUI();

    private final JFrame frame = new JFrame("HeadlessMc - 2.1.0");

    private final JPanel panel = new JPanel();
    private final JTextArea displayArea = new JTextArea();
    private final JScrollPane scrollPane = new JScrollPane(displayArea);
    private final JPasswordField inputField = new JPasswordField();
    private final JPanel inputPanel = new JPanel();

    private final AtomicReference<Consumer<String>> commandHandler = new AtomicReference<>(str -> displayArea.append("Still initializing, command ignored.\n"));
    private final List<String> history = new ArrayList<>();
    private volatile boolean initialized = false;
    private int historyIndex = -1;

    @Getter(AccessLevel.NONE)
    private final AtomicBoolean hidingPasswords = new AtomicBoolean(false);

    public void init() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            WebWrapperBridge.setUpdateListener(this::scheduleSizeChange);
            WebWrapperBridge.getWidthAndHeight(frame::setSize);
        } catch (Throwable t) {
            CheerpJMain.STDOUT.println("CheerpJ wrapper not available.");
            t.printStackTrace(CheerpJMain.STDOUT);
        }

        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Font monospacedFont = new Font("Monospaced", Font.PLAIN, 12);
        displayArea.setFont(monospacedFont);
        inputField.setFont(monospacedFont);

        Color darkBackground = new Color(34, 34, 34);
        Color lightForeground = new Color(187, 187, 187);
        panel.setForeground(lightForeground);
        panel.setBackground(darkBackground);

        displayArea.setEditable(false);
        displayArea.setBackground(darkBackground);
        displayArea.setForeground(lightForeground);
        DefaultCaret caret = (DefaultCaret) displayArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollPane.setBackground(darkBackground);
        scrollPane.setForeground(lightForeground);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setBackground(darkBackground);
        scrollPane.getVerticalScrollBar().setForeground(lightForeground);
        scrollPane.getHorizontalScrollBar().setBackground(darkBackground);
        scrollPane.getHorizontalScrollBar().setForeground(lightForeground);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.BLACK;
            }
        });

        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = Color.BLACK;
            }
        });

        inputField.setBackground(darkBackground);
        inputField.setForeground(lightForeground);

        inputField.setEchoChar((char) 0);
        inputField.addActionListener(e -> {
            char[] inputText = inputField.getPassword();
            String textString = new String(inputText);
            Arrays.fill(inputText, (char) 0);
            if (inputField.getEchoChar() != '*') {
                history.add(0, textString);
                while (history.size() > 128) {
                    history.remove(history.size() - 1);
                }

                displayArea.append(textString + "\n");
            }

            inputField.setText("");
            commandHandler.get().accept(textString);
        });

        addHistoryKeyListener();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        inputPanel.setBackground(darkBackground);
        inputPanel.setForeground(lightForeground);
        inputPanel.add(inputField, BorderLayout.CENTER);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
        initialized = true;
    }

    public void scheduleSizeChange(int width, int height) {
        SwingUtilities.invokeLater(() -> frame.setSize(width, height));
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

    private void addHistoryKeyListener() {
        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // NOP
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    historyIndex++;
                    if (historyIndex >= history.size()) {
                        historyIndex = history.size() - 1;
                    }

                    inputField.setText(history.get(historyIndex));
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    historyIndex--;
                    if (historyIndex < 0) {
                        historyIndex = -1;
                    }

                    if (historyIndex < 0) {
                        inputField.setText("");
                    } else {
                        inputField.setText(history.get(historyIndex));
                    }
                } else {
                    historyIndex = -1;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // NOP
            }
        });
    }

}
