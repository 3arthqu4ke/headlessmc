package me.earth.headlessmc.logging;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

@UtilityClass
public class LogLevelUtil {
    private static final Iterable<Level> LEVELS = Collections.unmodifiableList(
        Arrays.asList(
            OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL));

    public static Iterable<Level> getLevels() {
        return LEVELS;
    }

    public static void setLevel(Level level) {
        java.util.logging.Logger root = Logger.getLogger("");
        Handler[] handlers = root.getHandlers();
        for (Handler h : handlers) {
            h.setLevel(level);
        }
    }

}
