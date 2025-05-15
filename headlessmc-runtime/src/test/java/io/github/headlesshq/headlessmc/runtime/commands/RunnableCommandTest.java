package io.github.headlesshq.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.runtime.RuntimeTest;
import io.github.headlesshq.headlessmc.runtime.commands.reflection.RunnableCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RunnableCommandTest implements RuntimeTest {
    private final RunnableCommand command = new RunnableCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testRunnableCommand() {
        assertThrows(CommandException.class,
                     () -> command.execute("", "runnable"));
        assertThrows(CommandException.class,
                     () -> command.execute("", "runnable", "0"));
        assertThrows(CommandException.class,
                     () -> command.execute("", "runnable", "1024", ""));

        command.ctx.getVm().set(null, 0);
        assertNull(command.ctx.getVm().get(0));

        command.execute("", "runnable", "0", "boolean true 0");
        assertInstanceOf(Runnable.class, command.ctx.getVm().get(0));
        ((Runnable) command.ctx.getVm().get(0)).run();
        assertTrue((Boolean) command.ctx.getVm().get(0));

        command.execute("", "runnable", "0", "boolean true 1", "string false 2");
        assertInstanceOf(Runnable.class, command.ctx.getVm().get(0));
        ((Runnable) command.ctx.getVm().get(0)).run();
        assertTrue((Boolean) command.ctx.getVm().get(1));
        assertEquals("false", command.ctx.getVm().get(2));
    }

}
