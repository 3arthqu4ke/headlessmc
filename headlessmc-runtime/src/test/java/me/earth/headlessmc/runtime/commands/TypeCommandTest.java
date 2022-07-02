package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import me.earth.headlessmc.api.command.CommandContext;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.runtime.Runtime;
import me.earth.headlessmc.runtime.RuntimeTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TypeCommandTest implements RuntimeTest {
    private final Runtime runtime = getRuntime();
    private final CommandContext ctx = runtime.getCommandContext();
    private final TypeCommand cmd = TypeCommand.ofType(runtime, "test", s -> s);

    @Test
    @SneakyThrows
    public void testTypeCommand() {
        assertThrows(CommandException.class, () -> cmd.execute("test"));
        assertThrows(CommandException.class, () -> cmd.execute("test", "test"));

        runtime.getVm().set(false, 0);
        assertFalse((Boolean) runtime.getVm().get(0));

        ctx.execute("boolean true 0");
        assertTrue((Boolean) runtime.getVm().get(0));

        ctx.execute("string \"test test 0 test\" 0");
        assertEquals("test test 0 test", runtime.getVm().get(0));

        ctx.execute("char c 0");
        assertEquals('c', runtime.getVm().get(0));

        ctx.execute("byte 10 0");
        assertEquals((byte) 10, runtime.getVm().get(0));

        ctx.execute("short 11 0");
        assertEquals((short) 11, runtime.getVm().get(0));

        ctx.execute("int 12 0");
        assertEquals(12, runtime.getVm().get(0));

        ctx.execute("long 100000000000000000 0");
        assertEquals(100000000000000000L, runtime.getVm().get(0));

        ctx.execute("float 1.5 0");
        assertEquals(1.5f, runtime.getVm().get(0));

        ctx.execute("double 2.5 0");
        assertEquals(2.5, runtime.getVm().get(0));
    }

}
