package me.earth.headlessmc.logging;

import lombok.Cleanup;
import me.earth.headlessmc.config.HmcProperties;
import me.earth.headlessmc.util.ResourceUtil;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.*;

/**
 * A {@link StreamHandler} logging to {@link FileDescriptor#out} using a {@link
 * ThreadFormatter}.
 */
public class LoggingHandler extends StreamHandler {
    public LoggingHandler() {
        super(new PrintStream(new FileOutputStream(FileDescriptor.out)),
              new ThreadFormatter());
    }

    public static void apply() throws IOException {
        try {
            // check LoggingHandler can actually load the class from the SystemClassLoader
            ClassLoader.getSystemClassLoader().loadClass(LoggingHandler.class.getName());
            @Cleanup
            InputStream is = ResourceUtil.getHmcResource("logging.properties");
            LogManager.getLogManager().readConfiguration(is);
        } catch (Exception ignored) {
            Handler[] handlers = Logger.getLogger("").getHandlers();
            for (Handler handler : handlers) {
                if (handler != null && !(handler instanceof FileHandler)) {
                    Logger.getLogger("").removeHandler(handler);
                }
            }
            // add the Handler manually
            Logger.getLogger("").addHandler(new LoggingHandler());
        }

        String property = System.getProperty(HmcProperties.LOGLEVEL.getName());
        if (property == null || !LogLevelUtil.trySetLevel(property)) {
            LogLevelUtil.setLevel(Level.INFO);
        }
    }

    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

    @Override
    public void close() {
        flush();
    }

}