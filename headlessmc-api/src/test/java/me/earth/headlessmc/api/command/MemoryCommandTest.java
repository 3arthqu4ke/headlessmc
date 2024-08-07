package me.earth.headlessmc.api.command;

import lombok.val;
import me.earth.headlessmc.api.MockedHeadlessMc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MemoryCommandTest {
    @Test
    public void testMemoryCommand() {
        val command = new MemoryCommand(MockedHeadlessMc.INSTANCE);
        assertDoesNotThrow(() -> command.execute("memory"));
    }

}
