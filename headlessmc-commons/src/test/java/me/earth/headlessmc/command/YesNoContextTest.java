package me.earth.headlessmc.command;

import me.earth.headlessmc.MockedHeadlessMc;
import me.earth.headlessmc.api.HeadlessMc;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class YesNoContextTest {
    @Test
    public void testYesNoContext() {
        HeadlessMc hmc = MockedHeadlessMc.INSTANCE;
        boolean[] value = new boolean[]{false};
        YesNoContext ctx = new YesNoContext(result -> value[0] = result, hmc);
        hmc.setCommandContext(ctx);
        Assertions.assertEquals(ctx, hmc.getCommandContext());

        ctx.execute("y");
        Assertions.assertTrue(value[0]);
        ctx.execute("n");
        Assertions.assertFalse(value[0]);

        YesNoContext.goBackAfter(hmc, result -> value[0] = result);
        Assertions.assertNotEquals(ctx, hmc.getCommandContext());
        hmc.getCommandContext().execute("y");
        Assertions.assertTrue(value[0]);
        Assertions.assertEquals(ctx, hmc.getCommandContext());
    }

}
