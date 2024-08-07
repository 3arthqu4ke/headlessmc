package me.earth.headlessmc.api.command;

import me.earth.headlessmc.api.MockedHeadlessMc;
import me.earth.headlessmc.api.HeadlessMc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YesNoContextTest {
    @Test
    public void testYesNoContext() {
        HeadlessMc hmc = MockedHeadlessMc.INSTANCE;
        Boolean[] value = new Boolean[1];
        YesNoContext ctx = new YesNoContext(result -> value[0] = result, hmc);
        hmc.getCommandLineManager().setCommandContext(ctx);
        assertFalse(hmc.getCommandLineManager().isWaitingForInput());
        assertEquals(ctx, hmc.getCommandLineManager().getCommandContext());

        ctx.execute("t");
        assertFalse(hmc.getCommandLineManager().isWaitingForInput());
        assertEquals(ctx, hmc.getCommandLineManager().getCommandContext());
        assertNull(value[0]);

        ctx.execute("y");
        assertTrue(value[0]);
        ctx.execute("n");
        assertFalse(value[0]);

        assertFalse(hmc.getCommandLineManager().isWaitingForInput());
        YesNoContext.goBackAfter(hmc, result -> value[0] = result);
        assertTrue(hmc.getCommandLineManager().isWaitingForInput());
        assertNotEquals(ctx, hmc.getCommandLineManager().getCommandContext());

        hmc.getCommandLineManager().getCommandContext().execute("y");
        assertFalse(hmc.getCommandLineManager().isWaitingForInput());
        assertTrue(value[0]);
        assertEquals(ctx, hmc.getCommandLineManager().getCommandContext());
    }

    @Test
    public void testIterator() {
        assertFalse(new YesNoContext(result -> {
        }, MockedHeadlessMc.INSTANCE).iterator().hasNext());
    }

    @Test
    public void testWithException() {
        HeadlessMc hmc = MockedHeadlessMc.INSTANCE;
        boolean[] value = new boolean[]{false};
        YesNoContext ctx = new YesNoContext(result -> {
            value[0] = true;
            throw new CommandException("test");
        }, hmc);

        assertDoesNotThrow(() -> ctx.execute("y"));
        assertTrue(value[0]);
    }

}
