package io.github.headlesshq.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import lombok.val;
import io.github.headlesshq.headlessmc.runtime.RuntimeTest;
import io.github.headlesshq.headlessmc.runtime.commands.reflection.WhileCommand;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class WhileCommandTest implements RuntimeTest {
    private final WhileCommand command = new WhileCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testWhileCommand() {
        val value = new AtomicInteger();
        command.ctx.getVm().set((Supplier<AtomicInteger>) () -> {
            if (value.incrementAndGet() == 5) {
                assertDoesNotThrow(() -> command.ctx.getVm().set(false, 1));
            }

            return value;
        }, 0);
        command.ctx.getVm().set(true, 1);

        assertTrue((Boolean) command.ctx.getVm().get(1));
        assertEquals(0, value.get());
        assertNull(command.ctx.getVm().get(2));
        command.execute("", "while", "1", "method 0 get 2");
        assertSame(value, command.ctx.getVm().get(2));
        assertEquals(5, value.get());
        assertInstanceOf(Boolean.class, command.ctx.getVm().get(1));
        assertFalse((Boolean) command.ctx.getVm().get(1));
    }

}
