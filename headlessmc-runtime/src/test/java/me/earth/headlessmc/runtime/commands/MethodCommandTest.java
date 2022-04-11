package me.earth.headlessmc.runtime.commands;

import lombok.val;
import lombok.var;
import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.runtime.RuntimeTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MethodCommandTest extends RuntimeTest {
    private final MethodCommand mc = new MethodCommand(ctx);

    @Test
    public void testExceptions() {
        val type = CommandException.class;
        var exc = assertThrows(type, mc::execute);
        assertEquals("Specify an owner and name!", exc.getMessage());
        exc = assertThrows(type, () -> mc.execute(""));
        assertEquals("Specify an owner and name!", exc.getMessage());
        exc = assertThrows(type, () -> mc.execute("", ""));
        assertEquals("Specify an owner and name!", exc.getMessage());
        exc = assertThrows(type, () -> mc.execute("", "", ""));
        assertTrue(exc.getMessage().startsWith("Couldn't parse"));
        exc = assertThrows(type, () -> mc.execute("", "1", ""));
        assertTrue(exc.getMessage().startsWith("There's no Object at "));
        // TODO: bla bla bla
    }

}
