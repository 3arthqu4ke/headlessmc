package io.github.headlesshq.headlessmc.api.command;

import io.github.headlesshq.headlessmc.api.HeadlessMc;
import io.github.headlesshq.headlessmc.api.MockedHeadlessMc;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YesNoContextTest {
    @Test
    public void testYesNoContext() {
        HeadlessMc hmc = MockedHeadlessMc.INSTANCE;
        Boolean[] value = new Boolean[1];
        YesNoContext ctx = new YesNoContext(result -> value[0] = result, hmc);
        hmc.getCommandLine().setCommandContext(ctx);
        assertFalse(hmc.getCommandLine().isWaitingForInput());
        assertEquals(ctx, hmc.getCommandLine().getCommandContext());

        ctx.execute("t");
        assertFalse(hmc.getCommandLine().isWaitingForInput());
        assertEquals(ctx, hmc.getCommandLine().getCommandContext());
        assertNull(value[0]);

        ctx.execute("y");
        assertTrue(value[0]);
        ctx.execute("n");
        assertFalse(value[0]);

        assertFalse(hmc.getCommandLine().isWaitingForInput());
        YesNoContext.goBackAfter(hmc, result -> value[0] = result);
        assertTrue(hmc.getCommandLine().isWaitingForInput());
        assertNotEquals(ctx, hmc.getCommandLine().getCommandContext());

        hmc.getCommandLine().getCommandContext().execute("y");
        assertFalse(hmc.getCommandLine().isWaitingForInput());
        assertTrue(value[0]);
        assertEquals(ctx, hmc.getCommandLine().getCommandContext());
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
