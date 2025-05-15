package io.github.headlesshq.headlessmc.api.command;

import io.github.headlesshq.headlessmc.api.HeadlessMc;
import io.github.headlesshq.headlessmc.api.MockedHeadlessMc;
import io.github.headlesshq.headlessmc.api.command.impl.CommandContextCommand;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CommandContextCommandTest {
    @Test
    public void test() {
        MockedHeadlessMc hmc = new MockedHeadlessMc();
        TestCommand testCommand = new TestCommand(hmc);
        CommandContextImpl ctx = new CommandContextImpl(hmc);
        ctx.add(testCommand);

        CommandContextCommand command = new CommandContextCommand(hmc, "test", "test", ctx);
        CommandContextImpl ctx2 = new CommandContextImpl(hmc);
        ctx2.add(command);

        assertFalse(testCommand.used);
        ctx2.execute("test");

        assertFalse(testCommand.used);

        ctx2.execute("test dummy");
        assertTrue(testCommand.used);
        assertEquals(1, testCommand.usedArgs.length);
        assertEquals("dummy", testCommand.usedArgs[0]);
        testCommand.used = false;

        ctx2.execute("test dummy arg1");
        assertTrue(testCommand.used);
        assertEquals(2, testCommand.usedArgs.length);
        assertEquals("arg1", testCommand.usedArgs[1]);

        List<Map.Entry<String, String>> completions = ctx2.getCompletions("te");
        assertEquals(1, completions.size());
        assertEquals("test", completions.get(0).getKey());

        completions = ctx2.getCompletions("test");
        assertEquals(1, completions.size());
        assertEquals("test", completions.get(0).getKey());

        completions = ctx2.getCompletions("test ");
        assertEquals(1, completions.size());
        assertEquals("dummy", completions.get(0).getKey());

        completions = ctx2.getCompletions("test du");
        assertEquals(1, completions.size());
        assertEquals("dummy", completions.get(0).getKey());

        completions = ctx2.getCompletions("test dummy");
        assertEquals(1, completions.size());
        assertEquals("dummy", completions.get(0).getKey());

        completions = ctx2.getCompletions("test dummy a");
        assertEquals(1, completions.size());
        assertEquals("arg1", completions.get(0).getKey());
    }

    private static final class TestCommand extends AbstractCommand {
        private String[] usedArgs;
        private boolean used;

        public TestCommand(HeadlessMc ctx) {
            super(ctx, "dummy", "dummy");
        }

        @Override
        public void execute(String line, String... args) {
            usedArgs = args;
            used = true;
        }

        @Override
        public void getCompletions(String line, List<Map.Entry<String, @Nullable String>> completions, String... args) {
            super.getCompletions(line, completions, args);
            if (args.length == 2 && "arg1".startsWith( args[1])) {
                completions.add(new AbstractMap.SimpleEntry<>("arg1", ""));
            }
        }
    }

}
