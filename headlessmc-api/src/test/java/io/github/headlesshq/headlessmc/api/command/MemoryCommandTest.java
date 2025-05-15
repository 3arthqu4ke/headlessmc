package io.github.headlesshq.headlessmc.api.command;

import lombok.val;
import io.github.headlesshq.headlessmc.api.MockedHeadlessMc;
import io.github.headlesshq.headlessmc.api.command.impl.MemoryCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MemoryCommandTest {
    @Test
    public void testMemoryCommand() {
        val command = new MemoryCommand(MockedHeadlessMc.INSTANCE);
        assertDoesNotThrow(() -> command.execute("memory", "memory"));
    }

}
