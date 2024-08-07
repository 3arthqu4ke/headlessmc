package me.earth.headlessmc.logging.handlers;

import me.earth.headlessmc.logging.ThreadFormatter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.FileHandler;

public class HmcFileHandler extends FileHandler implements HmcHandler {
    public HmcFileHandler(Path path) throws IOException, SecurityException {
        super(path.toString());
        setFormatter(new ThreadFormatter());
    }

}
