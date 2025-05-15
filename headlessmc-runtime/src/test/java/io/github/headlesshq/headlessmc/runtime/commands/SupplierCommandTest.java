package io.github.headlesshq.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import lombok.val;
import io.github.headlesshq.headlessmc.api.command.CommandException;
import io.github.headlesshq.headlessmc.runtime.RuntimeTest;
import io.github.headlesshq.headlessmc.runtime.commands.reflection.SupplierCommand;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierCommandTest implements RuntimeTest {
    private final SupplierCommand command = new SupplierCommand(getRuntime());

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void testSupplierCommand() {
        assertThrows(CommandException.class,
                     () -> command.execute("", "supplier"));
        assertThrows(CommandException.class,
                     () -> command.execute("", "supplier", "boolean true 1"));
        assertThrows(CommandException.class,
                     () -> command.execute("", "supplier", "boolean true 1", "1"));

        command.ctx.getVm().set(null, 1);
        assertNull(command.ctx.getVm().get(1));

        command.execute("", "supplier", "boolean true 1", "1", "1");
        assertInstanceOf(Supplier.class, command.ctx.getVm().get(1));
        val supplier = (Supplier<Boolean>) command.ctx.getVm().get(1);
        assertTrue(supplier.get());
        assertInstanceOf(Boolean.class, command.ctx.getVm().get(1));
        assertTrue((boolean) command.ctx.getVm().get(1));
    }

}
