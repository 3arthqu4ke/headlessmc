package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.runtime.RuntimeTest;
import me.earth.headlessmc.runtime.commands.reflection.FunctionCommand;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionCommandTest implements RuntimeTest {
    private final FunctionCommand command = new FunctionCommand(getRuntime());

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void testFunctionCommand() {
        for (int i = 0; i < 4; i++) {
            command.ctx.getVm().set(null, i);
            assertNull(command.ctx.getVm().get(i));
        }

        command.execute("",
            "function",
            "if 0 \"boolean true 1\" \"boolean false 1\"", "2", "0", "3");

        assertInstanceOf(Function.class, command.ctx.getVm().get(2));
        val function = (Function<Boolean, ?>) command.ctx.getVm().get(2);
        val expected = new Object();
        command.ctx.getVm().set(expected, 3);

        assertSame(expected, function.apply(true));
        assertInstanceOf(Boolean.class, command.ctx.getVm().get(1));
        assertTrue((boolean) command.ctx.getVm().get(1));
    }

}
