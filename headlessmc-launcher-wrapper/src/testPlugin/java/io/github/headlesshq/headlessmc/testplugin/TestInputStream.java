package io.github.headlesshq.headlessmc.testplugin;

import io.github.headlesshq.headlessmc.api.process.WritableInputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

public class TestInputStream extends WritableInputStream {
    private final Deque<Consumer<PrintStream>> commands = new ArrayDeque<>();
    private boolean returnedSomething = false;

    public void add(String command) {
        add(p -> p.println(command));
    }

    public void add(Consumer<PrintStream> command) {
        commands.addLast(command);
    }

    @Override
    public int read() throws IOException {
        int result = super.read();
        if (result == -1) {
            while (!commands.isEmpty()) {
                commands.removeFirst().accept(getPrintStream());
                if (returnedSomething) {
                    returnedSomething = false;
                    break;
                } else {
                    result = read();
                    if (result != -1) {
                        break;
                    }
                }
            }
        }

        if (result != -1) {
            returnedSomething = true;
        }

        return result;
    }

}
