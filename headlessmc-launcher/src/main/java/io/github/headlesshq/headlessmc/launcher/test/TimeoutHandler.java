package io.github.headlesshq.headlessmc.launcher.test;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class TimeoutHandler implements AutoCloseable {
    private final ScheduledExecutorService executor;
    private final Runnable onTimeout;

    private ScheduledFuture<?> future;

    public boolean hasTimeout() {
        return future != null;
    }

    public void removeTimeout() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }

    public void setTimeout(long seconds) {
        if (future != null) {
            future.cancel(true);
        }

        future = executor.schedule(onTimeout, seconds, TimeUnit.SECONDS);
    }

    @Override
    public void close() {
        if (future != null) {
            future.cancel(true);
        }

        executor.shutdown();
    }

}
