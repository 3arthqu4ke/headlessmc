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
        hmc.setCommandContext(ctx);
        assertFalse(hmc.isWaitingForInput());
        assertEquals(ctx, hmc.getCommandContext());

        ctx.execute("t");
        assertFalse(hmc.isWaitingForInput());
        assertEquals(ctx, hmc.getCommandContext());
        assertNull(value[0]);

        ctx.execute("y");
        assertTrue(value[0]);
        ctx.execute("n");
        assertFalse(value[0]);

        assertFalse(hmc.isWaitingForInput());
        YesNoContext.goBackAfter(hmc, result -> value[0] = result);
        assertTrue(hmc.isWaitingForInput());
        assertNotEquals(ctx, hmc.getCommandContext());

        hmc.getCommandContext().execute("y");
        assertFalse(hmc.isWaitingForInput());
        assertTrue(value[0]);
        assertEquals(ctx, hmc.getCommandContext());
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
