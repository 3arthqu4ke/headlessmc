package me.earth.headlessmc.logging;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.*;

@CustomLog
@UtilityClass
public class LogLevelUtil {
    private static final Iterable<Level> LEVELS = Collections.unmodifiableList(
        Arrays.asList(
            OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL));

    public static Iterable<Level> getLevels() {
        return LEVELS;
    }

    /**
     * Attempts to parse the given String to a {@link Level} and sets that Level
     * via {@link #setLevel(Level)}.
     *
     * @param lvl the level to parse and set.
     * @return <tt>true</tt> if the level has been successfully parsed and set.
     */
    public static boolean trySetLevel(String lvl) {
        if ("debug".equalsIgnoreCase(lvl)) {
            setLevel(FINE);
        }

        try {
            setLevel(Level.parse(lvl));
            return true;
        } catch (Exception e) {
            log.error("Couldn't set level to " + lvl + " : " + e.getMessage());
            return false;
        }
    }

    public static void setLevel(Level level) {
        // TODO: when loaded by another classloader (fabric/forge) our
        //  LoggingHandler can't be found because the LogManager uses the
        //  SystemClassloader to discover handlers.
        java.util.logging.Logger root = Logger.getLogger("");
        Handler[] handlers = root.getHandlers();
        for (Handler h : handlers) {
            h.setLevel(level);
        }
    }

}
