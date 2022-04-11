package me.earth.headlessmc.logging;

import me.earth.headlessmc.api.LogsMessages;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class SimpleLog implements LogsMessages {
    private static final PrintStream OUT =
        new PrintStream(new FileOutputStream(FileDescriptor.out));

    @Override
    public void log(String message) {
        OUT.println(message);
    }

}
