package io.github.headlesshq.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.runtime.RuntimeTest;
import io.github.headlesshq.headlessmc.runtime.commands.reflection.CopyCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CopyCommandTest implements RuntimeTest {
    private final CopyCommand command = new CopyCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testCopyCommand() {
        assertThrows(CommandException.class,
                     () -> command.execute("copy", "copy"));
        assertThrows(CommandException.class,
                     () -> command.execute("copy nothing", "copy", "nothing"));

        command.ctx.getVm().set(1, 0);
        assertEquals(1, command.ctx.getVm().get(0));
        assertNull(command.ctx.getVm().get(1));

        command.execute("copy 0 1", "copy", "0", "1");
        assertEquals(1, command.ctx.getVm().get(0));
        assertEquals(1, command.ctx.getVm().get(1));
    }

}
