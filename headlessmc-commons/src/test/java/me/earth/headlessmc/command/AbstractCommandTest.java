package me.earth.headlessmc.command;

import lombok.val;
import me.earth.headlessmc.MockedHeadlessMc;
import me.earth.headlessmc.api.HeadlessMc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class AbstractCommandTest {
    @Test
    public void testBasicFunctionalities() {
        val command = DummyCommand.create();
        Assertions.assertEquals(DummyCommand.NAME, command.getName());
        Assertions.assertEquals(DummyCommand.DESC, command.getDescription());

        Assertions.assertEquals(command, command);
        Assertions.assertNotEquals(command, null);

        val command2 = DummyCommand.create();
        Assertions.assertEquals(command, command2);

        val command3 = new DummyCommand(command.ctx, "Test??", "tes");
        Assertions.assertNotEquals(command, command3);
    }

    @Test
    public void testArgs() {
        val command = DummyCommand.create();
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

        val itr = command.getArgs2Descriptions().iterator();
        Assertions.assertTrue(itr.hasNext());
        val entry = itr.next();
        Assertions.assertEquals("<testArg>", entry.getKey());
        Assertions.assertEquals("argDesc", entry.getValue());
        Assertions.assertFalse(itr.hasNext());
    }

    @Test
    public void testMatches() {
        val command = DummyCommand.create();
        Assertions.assertTrue(command.matches(DummyCommand.NAME));
        Assertions.assertTrue(command.matches(DummyCommand.NAME, "test"));
        Assertions.assertTrue(command.matches(DummyCommand.NAME, "test", "t"));
        Assertions.assertTrue(command.matches(DummyCommand.NAME.toUpperCase(Locale.ENGLISH)));
        Assertions.assertFalse(command.matches("test"));
    }

    private static final class DummyCommand extends AbstractCommand {
        public static final String NAME = "dummy name";
        public static final String DESC = "dummy desc";

        public DummyCommand(HeadlessMc ctx) {
            super(ctx, NAME, DESC);
        }

        public DummyCommand(HeadlessMc ctx, String name, String desc) {
            super(ctx, name, desc);
        }

        public static DummyCommand create() {
            return new DummyCommand(MockedHeadlessMc.INSTANCE);
        }

        @Override
        public void execute(String... args) {
            // dummy
        }
    }

}
