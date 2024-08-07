package me.earth.headlessmc.logging;

import lombok.CustomLog;
import lombok.Setter;
import me.earth.headlessmc.logging.handlers.HmcFileHandler;
import me.earth.headlessmc.logging.handlers.HmcHandler;
import me.earth.headlessmc.logging.handlers.HmcStreamHandler;
import org.jetbrains.annotations.Unmodifiable;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.logging.Level.*;

/**
 * Manages {@link me.earth.headlessmc.logging.Logger}s for HMC.
 * Removes all {@link Handler}s that are not {@link HmcHandler}s.
 */
@Setter
@CustomLog
public class LoggingService {
    private static final Iterable<Level> LEVELS = unmodifiableList(asList(OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL));
    private Supplier<PrintStream> streamFactory = () -> new PrintStream(new FileOutputStream(FileDescriptor.out), true);

    public void init() {
        clearOtherHandlers();
        addLoggingHandler();
        addFileHandler(Paths.get("HeadlessMC").resolve("headlessmc.log"));
        setLevelFromString(System.getProperty(LoggingProperties.LOG_LEVEL, "SEVERE"), false);
        setLevelFromString(System.getProperty(LoggingProperties.FILE_LOG_LEVEL, "DEBUG"), true);
    }

    public boolean setLevel(String level) {
        return setLevelFromString(level, false);
    }

    public void setLevel(Level level) {
        for (Handler handler : getRootHandlers()) {
            if (handler instanceof HmcHandler && !(handler instanceof FileHandler)) {
                handler.setLevel(level);
            }
        }
    }

    public void setFileHandlerLogLevel(Level level) {
        for (Handler handler : getRootHandlers()) {
            if (handler instanceof HmcHandler && handler instanceof FileHandler) {
                handler.setLevel(level);
            }
        }
    }

    private boolean setLevelFromString(String level, boolean fileHandler) {
        if ("debug".equalsIgnoreCase(level)) {
            return setLevelFromString("FINE", fileHandler);
        } else if ("error".equalsIgnoreCase(level)) {
            return setLevelFromString("SEVERE", fileHandler);
        }

        try {
            if (fileHandler) {
                setFileHandlerLogLevel(parse(level));
            } else {
                setLevel(parse(level));
            }

            return true;
        } catch (Exception e) {
            log.error("Couldn't set level to " + level + " : " + e.getMessage());
            return false;
        }
    }

    public void clearOtherHandlers() {
        for (Handler handler : getRootHandlers()) {
            if (handler != null) {
                Logger.getLogger("").removeHandler(handler);
            }
        }
    }

    public void addLoggingHandler() {
        Logger.getLogger("").addHandler(new HmcStreamHandler(streamFactory.get()));
    }

    public void addFileHandler(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Logger.getLogger("").addHandler(new HmcFileHandler(path));
        } catch (IOException e) {
            log.error("Failed to create directories for path " + path, e);
        }
    }

    private Handler[] getRootHandlers() {
        return Logger.getLogger("").getHandlers();
    }

    public @Unmodifiable Iterable<Level> getLevels() {
        return LEVELS;
    }

}
