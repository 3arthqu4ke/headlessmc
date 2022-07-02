package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.runtime.RuntimeTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PopCommandTest implements RuntimeTest {
    private final PopCommand command = new PopCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testPopCommand() {
        command.ctx.getVm().set(true, 0);
        assertTrue((Boolean) command.ctx.getVm().get(0));
        assertNull(command.ctx.getVm().get(1));
        assertThrows(CommandException.class, () -> command.execute("pop"));

        command.execute("pop", "0", "1");
        assertNull(command.ctx.getVm().get(0));
        assertTrue((Boolean) command.ctx.getVm().get(1));

        command.execute("pop", "1");
        assertNull(command.ctx.getVm().get(0));
        assertNull(command.ctx.getVm().get(1));
    }

}
