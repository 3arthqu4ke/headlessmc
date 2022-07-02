package me.earth.headlessmc.command;

import lombok.val;
import me.earth.headlessmc.MockedHeadlessMc;
import me.earth.headlessmc.api.HeadlessMc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractCommandTest {
    @Test
    public void testBasicFunctionalities() {
        val command = DummyCommand.getCommand();
        Assertions.assertEquals(DummyCommand.NAME, command.getName());
        Assertions.assertEquals(DummyCommand.DESC, command.getDescription());
        Assertions.assertFalse(command.getArgs().iterator().hasNext());

        val expectedArg = "<testArg>";
        val expectedArgDesc = "argDesc";
        command.args.put(expectedArg, expectedArgDesc);
        Assertions.assertTrue(command.getArgs().iterator().hasNext());
        Assertions.assertEquals(expectedArg,
                                command.getArgs().iterator().next());
        Assertions.assertEquals(expectedArgDesc,
                                command.getArgDescription(expectedArg));
        Assertions.assertEquals(expectedArgDesc,
                                command.getArgDescription("testArg"));
    }

    @Test
    public void testMatches() {
        val command = DummyCommand.getCommand();
        Assertions.assertTrue(command.matches(DummyCommand.NAME));
        Assertions.assertTrue(command.matches(DummyCommand.NAME, "test"));
        Assertions.assertTrue(command.matches(DummyCommand.NAME, "test", "t"));
        Assertions.assertTrue(command.matches(DummyCommand.NAME.toUpperCase()));
        Assertions.assertFalse(command.matches("test"));
    }

    private static final class DummyCommand extends AbstractCommand {
        public static final String NAME = "dummy name";
        public static final String DESC = "dummy desc";

        public DummyCommand(HeadlessMc ctx) {
            super(ctx, NAME, DESC);
        }

        @Override
        public void execute(String... args) {

        }

        public static DummyCommand getCommand() {
            return new DummyCommand(MockedHeadlessMc.INSTANCE);
        }
    }

}
