package me.earth.headlessmc.launcher;

import lombok.val;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.command.line.CommandLine;
import org.junit.jupiter.api.Test;

import java.io.IOError;
import java.util.concurrent.ThreadFactory;

import static me.earth.headlessmc.launcher.QuickExitCliHandler.checkQuickExit;
import static org.junit.jupiter.api.Assertions.*;

public class QuickExitCliHandlerTest {
    @Test
    public void testQuickExitCliHandler() {
        val launcher = LauncherMock.INSTANCE;
        val in = new MockCommandLine();
        launcher.getCommandLineManager().setCommandLineProvider(() -> in);

        assertFalse(checkQuickExit(launcher, ""));
        assertFalse(checkQuickExit(launcher, "test"));
        assertFalse(checkQuickExit(launcher, "test", "test"));
        assertFalse(checkQuickExit(launcher, "--command"));
        assertFalse(checkQuickExit(launcher, "--command", ""));
        assertFalse(checkQuickExit(launcher, "--command", "cli"));

        assertTrue(checkQuickExit(launcher, "--version"));
        assertFalse(in.called);
        assertFalse(launcher.getCommandLineManager().isQuickExitCli());

        val ctx = new MockedCommandContext();
        val waitForInput = "waitForInput";
        ctx.setCallback(str -> {
            if (str.equals(waitForInput)) {
                launcher.getCommandLineManager().setWaitingForInput(true);
            }
        });
        launcher.getCommandLineManager().setCommandContext(ctx);

        assertTrue(checkQuickExit(launcher, "--command", "test"));
        assertTrue(launcher.getCommandLineManager().isQuickExitCli());
        assertEquals("test", ctx.checkAndReset());
        launcher.getCommandLineManager().setQuickExitCli(false);
        assertFalse(in.called);

        assertTrue(checkQuickExit(launcher, "--command", "waitForInput"));
        assertTrue(launcher.getCommandLineManager().isQuickExitCli());
        assertEquals("waitForInput", ctx.checkAndReset());
        assertTrue(launcher.getCommandLineManager().isWaitingForInput());
        assertTrue(in.called);
    }

    private static final class MockCommandLine implements CommandLine {
        public boolean called;

        @Override
        public void listen(HeadlessMc hmc) throws IOError {
            called = true;
        }

        @Override
        public void listenAsync(HeadlessMc hmc, ThreadFactory factory) {
            called = true;
        }
    }

}
