package me.earth.headlessmc.logging.handlers;

import me.earth.headlessmc.logging.ThreadFormatter;

import java.io.PrintStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class HmcStreamHandler extends StreamHandler implements HmcHandler {
    public HmcStreamHandler(PrintStream printStream) {
        super(printStream, new ThreadFormatter());
    }

    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush(); // TODO: still necessary with PrintStream autoFlush true?
    }

    @Override
    public void close() {
        flush(); // TODO: still necessary with PrintStream autoFlush true?
    }

}
