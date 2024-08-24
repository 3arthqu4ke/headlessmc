package me.earth.headlessmc.launcher.command;

import me.earth.headlessmc.api.command.CommandException;
import me.earth.headlessmc.launcher.Launcher;
import me.earth.headlessmc.launcher.LauncherMock;
import me.earth.headlessmc.launcher.launch.LaunchException;
import me.earth.headlessmc.launcher.launch.LaunchOptions;
import me.earth.headlessmc.launcher.launch.ProcessFactory;
import me.earth.headlessmc.launcher.version.DummyVersion;
import me.earth.headlessmc.launcher.version.Version;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LaunchCommandTest {
    @Test
    public void testLaunchCommand() {
        LaunchCommand command = new LaunchCommand(LauncherMock.INSTANCE);
        assertThrows(CommandException.class, () -> command.execute("launch", "launch"));
        // More extensive testing is done in the launch.LaunchTest
    }

    @Test
    public void testLaunchCommandRetry() throws CommandException {
        Launcher launcher = LauncherMock.create();
        MockProcessFactory mockProcessFactory = new MockProcessFactory(launcher);
        launcher.setProcessFactory(mockProcessFactory);
        LaunchCommand command = new LaunchCommand(launcher);
        Version version = new DummyVersion("1.12.2-forge", new DummyVersion("1.12.2", null));
        AtomicInteger exitCode = new AtomicInteger();
        launcher.getExitManager().setExitManager(exitCode::set);
        assertEquals(0, mockProcessFactory.runs);

        command.execute(version, "launch", version.getName());
        assertEquals(1, mockProcessFactory.runs);
        assertEquals(-1, exitCode.get());
        mockProcessFactory.runs = 0;
        exitCode.set(0);

        command.execute(version, "launch", version.getName(), "--retries", "0");
        assertEquals(1, mockProcessFactory.runs);
        assertEquals(-1, exitCode.get());
        mockProcessFactory.runs = 0;
        exitCode.set(0);

        command.execute(version, "launch", version.getName(), "--retries", "1");
        assertEquals(2, mockProcessFactory.runs);
        assertEquals(-1, exitCode.get());
        mockProcessFactory.runs = 0;
        exitCode.set(0);

        command.execute(version, "launch", version.getName(), "--retries", "4");
        assertEquals(5, mockProcessFactory.runs);
        assertEquals(-1, exitCode.get());
        mockProcessFactory.runs = 0;
        exitCode.set(0);

        mockProcessFactory.dontFailOnRun = 4;
        command.execute(version, "launch", version.getName(), "--retries", "4");
        assertEquals(4, mockProcessFactory.runs);
        assertEquals(0, exitCode.get());
    }

    private static class MockProcessFactory extends ProcessFactory {
        private Integer dontFailOnRun;
        private int runs = 0;

        public MockProcessFactory(Launcher launcher) {
            super(launcher.getDownloadService(), launcher.getLauncherConfig(), launcher.getProcessFactory().getOs());
        }

        @Override
        public @Nullable Process run(LaunchOptions options) throws LaunchException {
            runs++;
            if (dontFailOnRun != null && runs == dontFailOnRun) {
                return null;
            }

            throw new LaunchException("Mock Factory");
        }
    }

}
