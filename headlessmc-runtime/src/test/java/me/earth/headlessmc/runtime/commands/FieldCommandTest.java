package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import lombok.val;
import me.earth.headlessmc.runtime.RuntimeTest;
import me.earth.headlessmc.runtime.TestClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldCommandTest implements RuntimeTest {
    private final FieldCommand command = new FieldCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testFieldCommand() {
        val testClass = new TestClass<>();
        command.ctx.getVm().set(testClass, 0);

        command.execute("field", "0", "FIRST_CTR", "1");
        assertEquals(TestClass.FIRST_CTR, command.ctx.getVm().get(1));

        command.execute("field", "0", "field_with_100", "1");
        assertEquals(100L, testClass.field_with_100);
        assertEquals(100L, command.ctx.getVm().get(1));

        command.ctx.getCommandContext().execute("long 200 2");
        assertEquals(200L, command.ctx.getVm().get(2));

        command.execute("field", "0", "field_with_100", "2", "-set");
        assertEquals(200L, testClass.field_with_100);
        assertEquals(100L, command.ctx.getVm().get(1));

        command.execute("field", "0", "field_with_100", "1");
        assertEquals(200L, testClass.field_with_100);
        assertEquals(200L, command.ctx.getVm().get(1));
    }

}
