package me.earth.headlessmc.command;

import lombok.val;
import me.earth.headlessmc.MockedHeadlessMc;
import me.earth.headlessmc.api.command.CommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HelpCommandTest {
    @Test
    public void testHelpCommand() {
        val command = new HelpCommand(MockedHeadlessMc.INSTANCE);
        val ctx = new CommandContextImpl(MockedHeadlessMc.INSTANCE);
        MockedHeadlessMc.INSTANCE.setCommandContext(ctx);

        assertDoesNotThrow(() -> command.execute("help"));
        ctx.add(command);

        assertDoesNotThrow(() -> command.execute("help"));
        assertThrows(CommandException.class,
                     () -> command.execute("help", "dummy"));

        ctx.add(new DummyCommand());
        assertDoesNotThrow(() -> command.execute("help"));
        assertDoesNotThrow(() -> command.execute("help", "dummy"));
        assertThrows(CommandException.class, () -> command.execute(
            "help", "dummy", "some arg"));
        assertDoesNotThrow(() -> command.execute("help", "dummy", "dummyArg"));
    }

    private static final class DummyCommand extends AbstractCommand {
        public DummyCommand() {
            super(MockedHeadlessMc.INSTANCE, "dummy", "dummy");
            args.put("dummyArg", "dummyDesc");
        }

        @Override
        public void execute(String... args) {
            // dummy
        }
    }

}
