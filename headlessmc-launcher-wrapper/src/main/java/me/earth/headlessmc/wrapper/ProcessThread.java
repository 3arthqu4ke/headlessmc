package me.earth.headlessmc.wrapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

// We must start the Thread up here in the LauncherWrapper
// If we do it from HeadlessMc it inherits the AccessControlContext,
// which refrences the LauncherWrapper TransformingClassloader that loaded HeadlessMc
// causing it to never get garbage collected.
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessThread {
    private static final ProcessThread INSTANCE = new ProcessThread();

    private final AtomicReference<Process> process = new AtomicReference<>();
    private final AtomicReference<Thread> thread = new AtomicReference<>();

    public void start() {
        synchronized (process) {
            if (thread.get() == null) {
                Thread thread = new Thread(() -> {
                    try {
                        Process process;
                        synchronized (this.process) {
                            this.process.wait();
                            process = this.process.get();
                            if (process == null) {
                                throw new IllegalStateException("Process is null");
                            }
                        }

                        System.gc();
                        System.exit(process.waitFor());
                    } catch (InterruptedException ignored) {
                        // end
                    }
                });

                thread.setName("ProcessThread");
                thread.start();
                this.thread.set(thread);
            } else {
                throw new IllegalStateException("Thread already started");
            }
        }
    }

    public void mainThreadEnded() {
        synchronized (process) {
            if (thread.get() != null && process.get() == null) {
                thread.get().interrupt();
            }
        }
    }

    public void setProcess(Process process) {
        synchronized (this.process) {
            if (this.process.get() == null) {
                this.process.set(process);
                this.process.notifyAll();
            }
        }
    }

    @SuppressWarnings("unused") // see me.earth.headlessmc.launcher.command.AbstractLaunchProcessLifecycle
    public static void setProcessInstance(Process process) {
        getInstance().setProcess(process);
    }

    public static ProcessThread getInstance() {
        return INSTANCE;
    }

}
