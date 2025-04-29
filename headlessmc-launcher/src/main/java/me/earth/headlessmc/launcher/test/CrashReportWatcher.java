package me.earth.headlessmc.launcher.test;

import lombok.CustomLog;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@CustomLog
public class CrashReportWatcher implements AutoCloseable {
    private final CopyOnWriteArrayList<Consumer<Path>> listeners = new CopyOnWriteArrayList<>();
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final AtomicBoolean started = new AtomicBoolean(false);

    private final WatchService watchService;
    private final ExecutorService executor;
    private final boolean shutDownExecutorService;

    public CrashReportWatcher(Path directory,
                              ExecutorService executorService,
                              boolean shutDownExecutorService) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.executor = executorService;
        this.shutDownExecutorService = shutDownExecutorService;
        directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        executorService.submit(() -> {
            synchronized (started) {
                started.set(true);
                started.notifyAll();
            }

            try {
                while (!closed.get()) {
                    WatchKey key;
                    try {
                        key = watchService.take();
                    } catch (InterruptedException e) {
                        log.info("CrashReport Watcher interrupted");
                        watchService.close();
                        return;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
                            Path path = (Path) event.context();
                            for (Consumer<Path> listener : listeners) {
                                listener.accept(path);
                            }
                        }
                    }

                    key.reset();
                }
            } catch (ClosedWatchServiceException ignored) {
                log.debug("WatchService closed");
            } catch (Exception e) {
                log.error("CrashReport Watcher encountered Exception", e);
            }
        });
    }

    public void addListener(Consumer<Path> listener) {
        listeners.add(listener);
    }

    public void waitForStart() throws InterruptedException {
        synchronized (started) {
            if (!started.get()) {
                started.wait();
            }
        }
    }

    @Override
    public void close() throws IOException {
        closed.set(true);
        watchService.close();
        if (shutDownExecutorService) {
            executor.shutdown();
        }
    }

    public static CrashReportWatcher forGameDir(Path gameDir) throws IOException {
        Path crashReportsDir = gameDir.resolve("crash-reports");
        if (!Files.exists(crashReportsDir)) {
            Files.createDirectories(crashReportsDir);
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("CrashReportWatcher");
            return thread;
        });

        return new CrashReportWatcher(crashReportsDir, executorService, true);
    }

}
