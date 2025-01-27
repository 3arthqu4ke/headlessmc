package me.earth.headlessmc.launcher.launch;

import lombok.Getter;
import lombok.Setter;
import me.earth.headlessmc.launcher.auth.AuthException;
import me.earth.headlessmc.launcher.download.DownloadService;
import me.earth.headlessmc.launcher.files.FileManager;
import me.earth.headlessmc.launcher.files.LauncherConfig;
import me.earth.headlessmc.launcher.instrumentation.Instrumentation;
import me.earth.headlessmc.launcher.version.Version;
import me.earth.headlessmc.os.OS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class MockProcessFactory extends ProcessFactory {
    private Instrumentation instrumentation;
    private ProcessBuilder builder;
    private LaunchOptions options;

    public MockProcessFactory(DownloadService downloadService, LauncherConfig launcherConfig, OS os) {
        super(downloadService, launcherConfig, os);
    }

    @Override
    public Process run(LaunchOptions options, Instrumentation instrumentation)
        throws IOException, LaunchException, AuthException {
        setInstrumentation(instrumentation);
        setOptions(options);
        return super.run(options, instrumentation);
    }

    @Override
    protected boolean checkZipIntact(File file) {
        return true;
    }

    @Override
    protected void downloadAssets(LaunchOptions options, FileManager files, Version version) {
        // dummy
    }

    @Override
    protected Process run(ProcessBuilder builder) {
        this.setBuilder(builder);
        return new MockProcess();
    }

    private static final class MockProcess extends Process {
        private boolean alive = true;

        @Override
        public boolean waitFor(long timeout, TimeUnit unit) {
            return false;
        }

        @Override
        public Process destroyForcibly() {
            return this;
        }

        @Override
        public boolean isAlive() {
            return alive;
        }

        @Override
        public OutputStream getOutputStream() {
            return new OutputStream() {
                @Override
                public void write(int b) {
                    // dummy
                }
            };
        }

        @Override
        public InputStream getInputStream() {
            return new InputStream() {
                @Override
                public int read() {
                    return -1;
                }
            };
        }

        @Override
        public InputStream getErrorStream() {
            return new InputStream() {
                @Override
                public int read() {
                    return -1;
                }
            };
        }

        @Override
        public int waitFor() {
            return 0;
        }

        @Override
        public int exitValue() {
            return 0;
        }

        @Override
        public void destroy() {
            this.alive = false;
        }
    }

}
