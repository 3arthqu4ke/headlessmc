package io.github.headlesshq.headlessmc.logging.handlers;

import io.github.headlesshq.headlessmc.logging.ThreadFormatter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;

public class HmcFileHandler extends FileHandler implements HmcHandler {
    @Deprecated
    public HmcFileHandler(Path path) throws IOException, SecurityException {
        this(path, new ThreadFormatter());
    }

    public HmcFileHandler(Path path, Formatter formatter) throws IOException, SecurityException {
        super(path.toString());
        setFormatter(formatter);
    }

}
