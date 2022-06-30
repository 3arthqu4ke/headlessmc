package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.runtime.RuntimeTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class MethodCommandTest implements RuntimeTest {
    private final MethodCommand command = new MethodCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testExceptions() {
        val called = new AtomicBoolean();
        assertFalse(called.get());
        command.ctx.getVm().set(called, 0);
        command.ctx.getVm().set(true, 1);

        command.execute("method", "0", "set", "1", "1");
        assertTrue(called.get());
        assertNull(command.ctx.getVm().get(1));

        // TODO: test methods with same name!
    }

}
