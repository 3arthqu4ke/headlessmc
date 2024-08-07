package me.earth.headlessmc.launcher.command.login;

import lombok.val;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.command.LaunchContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PasswordContextTest {
    @Test
    public void testPasswordContext() {
        val launcher = LauncherMock.INSTANCE;
        val ctx = new LaunchContext(launcher);
        launcher.getCommandLineManager().setCommandContext(ctx);
        launcher.getCommandLineManager().setHidingPasswords(false);
        launcher.getCommandLineManager().setWaitingForInput(false);
        Assertions.assertFalse(launcher.getCommandLineManager().isWaitingForInput());
        Assertions.assertEquals(ctx, launcher.getCommandLineManager().getCommandContext());
        Assertions.assertFalse(launcher.getCommandLineManager().isHidingPasswords());

        /*launcher.getCommandLineManager().getCommandContext().execute("login test");
        Assertions.assertInstanceOf(PasswordContext.class,
                                    launcher.getCommandLineManager().getCommandContext());
        Assertions.assertTrue(launcher.isHidingPasswords());
        Assertions.assertTrue(launcher.isWaitingForInput());

        launcher.getCommandLineManager().getCommandContext().execute("abort");
        Assertions.assertFalse(launcher.isHidingPasswords());
        Assertions.assertEquals(ctx, launcher.getCommandLineManager().getCommandContext());
        Assertions.assertFalse(launcher.isWaitingForInput());*/
    }

}
