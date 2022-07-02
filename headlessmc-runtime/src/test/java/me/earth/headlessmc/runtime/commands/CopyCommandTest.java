package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.runtime.RuntimeTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CopyCommandTest implements RuntimeTest {
    private final CopyCommand command = new CopyCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testCopyCommand() {
        assertThrows(CommandException.class,
                     () -> command.execute("copy"));
        assertThrows(CommandException.class,
                     () -> command.execute("copy", "nothing"));

        command.ctx.getVm().set(1, 0);
        assertEquals(1, command.ctx.getVm().get(0));
        assertNull(command.ctx.getVm().get(1));

        command.execute("copy", "0", "1");
        assertEquals(1, command.ctx.getVm().get(0));
        assertEquals(1, command.ctx.getVm().get(1));
    }

}
