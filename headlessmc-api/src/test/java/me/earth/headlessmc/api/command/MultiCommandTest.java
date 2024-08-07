package me.earth.headlessmc.api.command;

import lombok.val;
import me.earth.headlessmc.api.MockedHeadlessMc;
import me.earth.headlessmc.api.HeadlessMc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MultiCommandTest {
    @Test
    public void testMultiCommand() {
        val ctx = new CommandContextImpl(MockedHeadlessMc.INSTANCE);
        val command1 = new TestCommand(MockedHeadlessMc.INSTANCE, "test1");
        val command2 = new TestCommand(MockedHeadlessMc.INSTANCE, "test2");
        val command3 = new TestCommand2(MockedHeadlessMc.INSTANCE, "test3");
        ctx.add(command1);
        ctx.add(command2);
        ctx.add(command3);
        ctx.add(new MultiCommand(MockedHeadlessMc.INSTANCE));
        MockedHeadlessMc.INSTANCE.setCommandContext(ctx);

        ctx.execute("multi test1");
        assertTrue(command1.value);
        assertFalse(command2.value);
        command1.value = false;

        ctx.execute("multi test1 test2");
        assertTrue(command1.value);
        assertTrue(command2.value);
        command1.value = false;
        command2.value = false;

        ctx.execute("multi test1 test2 \"test3 \\\"value with space\\\"");
        assertTrue(command1.value);
        assertTrue(command2.value);
        assertEquals("value with space", command3.value);
    }

    private static final class TestCommand extends AbstractCommand {
        public boolean value = false;

        public TestCommand(HeadlessMc ctx, String name) {
            super(ctx, name, "desc");
        }

        @Override
        public void execute(String... args) {
            this.value = true;
        }
    }

    private static final class TestCommand2 extends AbstractCommand {
        public String value = "test";

        public TestCommand2(HeadlessMc ctx, String name) {
            super(ctx, name, "desc");
        }

        @Override
        public void execute(String... args) {
            this.value = args[1];
        }
    }

}
