package me.earth.headlessmc.runtime.commands;

import lombok.SneakyThrows;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.runtime.RuntimeTest;
import me.earth.headlessmc.runtime.TestClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NewCommandTest implements RuntimeTest {
    private final NewCommand command = new NewCommand(getRuntime());

    @Test
    @SneakyThrows
    public void testNewCommand() {
        val ctx = command.ctx.getCommandContext();
        val vm = command.ctx.getVm();

        ctx.execute("class " + TestClass.class.getName() + " 0");
        assertEquals(vm.get(0), TestClass.class);

        ctx.execute("new 0 1");
        assertInstanceOf(TestClass.class, vm.get(1));
        var testClass = ((TestClass<?>) vm.get(1));
        assertEquals(TestClass.NOARGS_CTR, testClass.ctr);

        ctx.execute("string test 2");
        assertEquals("test", vm.get(2));

        ctx.execute("new 0 1 2");
        assertSame(testClass, vm.get(1));

        ctx.execute("new 0 1 java.lang.String");
        assertSame(testClass, vm.get(1));

        ctx.execute("new 0 1 java.lang.String 2");
        assertNotSame(testClass, vm.get(1));
        testClass = ((TestClass<?>) vm.get(1));
        assertEquals(TestClass.STRING_CTR, testClass.ctr);

        ctx.execute("string dummy 4");
        assertEquals("dummy", vm.get(4));

        ctx.execute("new 0 1 java.lang.String,java.lang.Object 2 4");
        assertNotSame(testClass, vm.get(1));
        testClass = ((TestClass<?>) vm.get(1));
        assertEquals(TestClass.TWO_ARGS_CTR, testClass.ctr);
        assertEquals("dummy", testClass.secondVal);
    }

}
