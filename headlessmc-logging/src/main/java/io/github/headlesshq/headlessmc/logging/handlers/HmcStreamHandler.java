package io.github.headlesshq.headlessmc.logging.handlers;

import io.github.headlesshq.headlessmc.logging.ThreadFormatter;

import java.io.PrintStream;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class HmcStreamHandler extends StreamHandler implements HmcHandler {
    @Deprecated
    public HmcStreamHandler(PrintStream printStream) {
        this(printStream, new ThreadFormatter());
    }

    public HmcStreamHandler(PrintStream printStream, Formatter formatter) {
        super(printStream, formatter);
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
