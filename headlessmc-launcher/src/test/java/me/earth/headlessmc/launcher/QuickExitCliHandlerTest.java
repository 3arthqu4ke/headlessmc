package me.earth.headlessmc.launcher;

import lombok.val;
import me.earth.headlessmc.api.QuickExitCli;
import me.earth.headlessmc.api.command.line.Listener;
import org.junit.jupiter.api.Test;

import java.io.IOError;
import java.util.concurrent.ThreadFactory;

import static me.earth.headlessmc.launcher.QuickExitCliHandler.checkQuickExit;
import static org.junit.jupiter.api.Assertions.*;

public class QuickExitCliHandlerTest {
    @Test
    public void testQuickExitCliHandler() {
        val launcher = LauncherMock.INSTANCE;
        val in = new MockListener();

        assertFalse(checkQuickExit(launcher, in, ""));
        assertFalse(checkQuickExit(launcher, in, "test"));
        assertFalse(checkQuickExit(launcher, in, "test", "test"));
        assertFalse(checkQuickExit(launcher, in, "--command"));
        assertFalse(checkQuickExit(launcher, in, "--command", ""));
        assertFalse(checkQuickExit(launcher, in, "--command", "cli"));

        assertTrue(checkQuickExit(launcher, in, "--version"));
        assertFalse(in.called);
        assertFalse(launcher.isQuickExitCli());

        val ctx = new MockedCommandContext();
        val waitForInput = "waitForInput";
        ctx.setCallback(str -> {
            if (str.equals(waitForInput)) {
                launcher.setWaitingForInput(true);
            }
        });
        launcher.setCommandContext(ctx);

        assertTrue(checkQuickExit(launcher, in, "--command", "test"));
        assertTrue(launcher.isQuickExitCli());
        assertEquals("test", ctx.checkAndReset());
        launcher.setQuickExitCli(false);
        assertFalse(in.called);

        assertTrue(checkQuickExit(launcher, in, "--command", "waitForInput"));
        assertTrue(launcher.isQuickExitCli());
        assertEquals("waitForInput", ctx.checkAndReset());
        assertTrue(launcher.isWaitingForInput());
        assertTrue(in.called);
    }

    private static final class MockListener implements Listener {
        public boolean called;

        @Override
        public void listen(QuickExitCli context) throws IOError {
            called = true;
        }

        @Override
        public void listenAsync(QuickExitCli context, ThreadFactory factory) {
            called = true;
        }
    }

}
