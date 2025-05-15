package io.github.headlesshq.headlessmc.launcher.test;

import io.github.headlesshq.headlessmc.launcher.LauncherMock;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrashReportWatcherTest {
    @Test
    public void testCrashReportWatcher() throws IOException, InterruptedException {
        Path path = LauncherMock.INSTANCE.getMcFiles().getBase().toPath();
        try (CrashReportWatcher crashReportWatcher = CrashReportWatcher.forGameDir(path)) {
            Path crashReportsDir = path.resolve("crash-reports");
            AtomicReference<Path> pathRef = new AtomicReference<>();
            crashReportWatcher.addListener(p -> {
                synchronized (pathRef) {
                    pathRef.set(p);
                    pathRef.notifyAll();
                }
            });

            synchronized (pathRef) {
                Files.createFile(crashReportsDir.resolve("test.txt"));
                pathRef.wait(60_000);
            }

            assertEquals("test.txt", pathRef.get().getFileName().toString());
        }
    }

}
