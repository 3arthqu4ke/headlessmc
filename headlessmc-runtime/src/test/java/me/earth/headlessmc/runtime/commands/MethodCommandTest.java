package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.runtime.RuntimeTest;
import me.earth.headlessmc.runtime.TestClass;
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
        val testClass = new TestClass<>();
        testClass.value = null;
        command.ctx.getVm().set(testClass, 0);
        command.ctx.getVm().set(null, 1);
        assertThrows(CommandException.class, () -> command.execute(
            "method", "0", "methodWithSameName", "1", "1"));
        assertNull(command.ctx.getVm().get(1));

        command.execute("method", "0", "methodWithSameName", "", "1", "1");
        assertEquals(TestClass.METHOD_WO_ARGS, command.ctx.getVm().get(1));
        assertNull(testClass.parameter);

        command.execute("method", "0", "methodWithSameName",
                        "java.lang.String", "2", "1");
        assertEquals(command.ctx.getVm().get(1), testClass.parameter);
        assertEquals(TestClass.METHOD_W_ARGS, command.ctx.getVm().get(2));
    }

}
