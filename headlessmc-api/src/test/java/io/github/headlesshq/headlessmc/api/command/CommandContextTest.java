package io.github.headlesshq.headlessmc.api.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import io.github.headlesshq.headlessmc.api.MockedHeadlessMc;
import io.github.headlesshq.headlessmc.api.command.impl.MultiCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

public class CommandContextTest {
    // own instance because we want to test the log
    private static final MockedHeadlessMc HMC = new MockedHeadlessMc();

    @Test
    public void testCommandContextImpl() {
        val ctx = new CommandContextImpl(HMC);
        Assertions.assertDoesNotThrow(() -> ctx.execute("test"));

        ctx.add(TestCommands.COMMAND_1);
        ctx.add(TestCommands.COMMAND_2);
        ctx.add(new MultiCommand(HMC));
        HMC.getCommandLine().setCommandContext(ctx);
        TestCommands.COMMAND_1.setUsed(false);
        TestCommands.COMMAND_2.setUsed(false);
        Assertions.assertEquals(3, ctx.commands.size());
        Assertions.assertEquals(TestCommands.COMMAND_1, ctx.commands.get(0));
        Assertions.assertEquals(TestCommands.COMMAND_2, ctx.commands.get(1));

        Assertions.assertFalse(TestCommands.COMMAND_1.isUsed());
        Assertions.assertFalse(TestCommands.COMMAND_2.isUsed());

        ctx.execute("");
        Assertions.assertFalse(TestCommands.COMMAND_1.isUsed());
        Assertions.assertFalse(TestCommands.COMMAND_2.isUsed());

        ctx.execute("Command1");
        Assertions.assertTrue(TestCommands.COMMAND_1.isUsed());
        Assertions.assertFalse(TestCommands.COMMAND_2.isUsed());
        TestCommands.COMMAND_1.setUsed(false);

        ctx.execute("Command2");
        Assertions.assertFalse(TestCommands.COMMAND_1.isUsed());
        Assertions.assertTrue(TestCommands.COMMAND_2.isUsed());
        TestCommands.COMMAND_2.setUsed(false);

        ctx.execute("multi Command2 Command1");
        Assertions.assertTrue(TestCommands.COMMAND_1.isUsed());
        Assertions.assertTrue(TestCommands.COMMAND_2.isUsed());
        TestCommands.COMMAND_1.setUsed(false);
        TestCommands.COMMAND_2.setUsed(false);

        HMC.setLog(null);
        Assertions.assertNull(HMC.getLog());
        ctx.execute("test");
        Assertions.assertFalse(TestCommands.COMMAND_1.isUsed());
        Assertions.assertFalse(TestCommands.COMMAND_2.isUsed());
        Assertions.assertEquals(
            "Couldn't find command for '[test]', did you mean 'multi'?",
            HMC.getLog());
    }

    @RequiredArgsConstructor
    private enum TestCommands implements Command {
        COMMAND_1("command1"),
        COMMAND_2("command2");

        @Getter
        private final String name;
        @Getter
        @Setter
        private boolean used;

        @Override
        public void execute(String line, String... args) {
            setUsed(true);
        }

        @Override
        public boolean matches(String line, String... args) {
            return new AbstractCommand(HMC, name, "") {
                @Override
                public void execute(String line, String... args) {
                }
            }.matches(line, args);
        }

        @Override
        public Iterable<String> getArgs() {
            return Collections.emptyList();
        }

        @Override
        public String getArgDescription(String arg) {
            return "";
        }

        @Override
        public Iterable<Map.Entry<String, String>> getArgs2Descriptions() {
            return Collections.emptyList();
        }

        @Override
        public String getDescription() {
            return "";
        }
    }

}
