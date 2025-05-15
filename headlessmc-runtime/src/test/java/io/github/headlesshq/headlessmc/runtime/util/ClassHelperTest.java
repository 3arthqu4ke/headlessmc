package io.github.headlesshq.headlessmc.runtime.util;

import io.github.headlesshq.headlessmc.api.LogsMessages;
import io.github.headlesshq.headlessmc.runtime.reflection.ClassHelper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassHelperTest {
    @Test
    @Disabled // test if different on different JVM versions
    public void testDumpClass() {
        TestLogsMessages testLogsMessages = new TestLogsMessages();
        ClassHelper.of(TestLogsMessages.class).dump(testLogsMessages, true);
        assertEquals("-----------------------------------", testLogsMessages.messages.get(0));
        assertEquals("io.github.headlesshq.headlessmc.runtime.util.ClassHelperTest$TestLogsMessages : java.lang.Object, io.github.headlesshq.headlessmc.api.LogsMessages", testLogsMessages.messages.get(1));
        assertEquals("--------------Fields---------------", testLogsMessages.messages.get(2));

        ClassHelper.of(Object.class).dump(testLogsMessages, true);
        // assert

        ClassHelper.of(Object.class).dump(testLogsMessages, false);
        // assert
    }

    public static class TestLogsMessages implements LogsMessages {
        public final List<String> messages = new ArrayList<>();

        @Override
        public void log(String message) {
            messages.add(message);
        }
    }

}
