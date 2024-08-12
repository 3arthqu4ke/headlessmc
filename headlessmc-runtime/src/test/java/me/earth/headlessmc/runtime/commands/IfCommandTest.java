package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import me.earth.headlessmc.runtime.RuntimeTest;
import me.earth.headlessmc.runtime.commands.reflection.IfCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IfCommandTest implements RuntimeTest {
    private final IfCommand command = new IfCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testIfCommand() {
        command.ctx.getVm().set(null, 0);
        assertNull(command.ctx.getVm().get(0));

        // TODO: this should cause a CommandException?
        command.execute("", "if");
        assertNull(command.ctx.getVm().get(0));
        command.execute("", "if", "0");
        assertNull(command.ctx.getVm().get(0));

        command.execute("", "if", "0", "boolean false 0");
        assertNull(command.ctx.getVm().get(0));
        command.execute("", "if", "0", "boolean false 0", "boolean true 0");
        assertTrue((Boolean) command.ctx.getVm().get(0));
        command.execute("", "if", "0", "boolean false 0", "boolean true 0");
        assertFalse((Boolean) command.ctx.getVm().get(0));
    }

}
