package me.earth.headlessmc.launcher.command.login;

import lombok.val;
import lombok.var;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.command.LaunchContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordContextTest {
    @Test
    public void testPasswordContext() {
        val launcher = LauncherMock.INSTANCE;
        val ctx = new LaunchContext(launcher);
        launcher.setCommandContext(ctx);
        launcher.setHidingPasswords(false);
        Assertions.assertEquals(ctx, launcher.getCommandContext());
        Assertions.assertFalse(launcher.isHidingPasswords());

        launcher.getCommandContext().execute("login test");
        Assertions.assertInstanceOf(PasswordContext.class,
                                    launcher.getCommandContext());
        Assertions.assertTrue(launcher.isHidingPasswords());

        launcher.getCommandContext().execute("abort");
        Assertions.assertFalse(launcher.isHidingPasswords());
        Assertions.assertEquals(ctx, launcher.getCommandContext());
    }

}
