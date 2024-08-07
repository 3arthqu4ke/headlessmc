package me.earth.headlessmc.api.command.line;

import me.earth.headlessmc.api.QuickExitCli;

import java.io.IOError;
import java.util.concurrent.ThreadFactory;

@FunctionalInterface
public interface Listener {
    ThreadFactory DEFAULT_THREAD_FACTORY = runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName("HeadlessMc-CommandLine");
        return thread;
    };

    void listen(QuickExitCli context) throws IOError;

    default void listenAsync(QuickExitCli context) {
        listenAsync(context, DEFAULT_THREAD_FACTORY);
    }

    default void listenAsync(QuickExitCli context, ThreadFactory factory) {
        factory.newThread(() -> listen(context)).start();
    }

}
