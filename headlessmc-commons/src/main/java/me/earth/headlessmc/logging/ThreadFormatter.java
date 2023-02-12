package me.earth.headlessmc.logging;

import lombok.val;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Produces log messages like this: {@literal "[23:18:58] [main/INFO] [Test]:
 * Test"}, because Javas Formatter doesn't support getting the Thread name.
 */
public class ThreadFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        val dt = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        // we want to be compatible with Java 8
        @SuppressWarnings({"deprecation", "RedundantSuppression"})
        val thread = getThread(record.getThreadID());
        val sb = new StringBuilder(getLength(record, thread)).append("[");
        appendTimeNumber(sb, dt.getHour());
        sb.append(':');
        appendTimeNumber(sb, dt.getMinute());
        sb.append(':');
        appendTimeNumber(sb, dt.getSecond());
        return sb.append("] [")
                 .append(thread)
                 .append('/')
                 .append(record.getLevel())
                 .append("] [")
                 .append(record.getLoggerName())
                 .append("]: ")
                 .append(record.getMessage())
                 .append('\n')
                 .toString();
    }

    private String getThread(long threadId) {
        // could we get any performance benefit from caching this?
        return Thread.getAllStackTraces()
                     .keySet()
                     .stream()
                     .filter(t -> t.getId() == threadId)
                     .map(Thread::getName)
                     .findFirst()
                     .orElse("Unknown-Thread");
    }

    private void appendTimeNumber(StringBuilder sb, int number) {
        if (number < 10) {
            sb.append('0').append(number);
        } else {
            sb.append(number);
        }
    }

    private int getLength(LogRecord record, String thread) {
        return 20 // "[::] [/] []: \n"
            + thread.length()
            + record.getLevel().getName().length()
            + (record.getMessage() == null ? 4 : record.getMessage().length())
            + record.getLoggerName().length();
    }

}
