package me.earth.headlessmc.api.command.line;

import me.earth.headlessmc.api.HeadlessMc;

import java.io.IOError;
import java.util.concurrent.ThreadFactory;

@FunctionalInterface
public interface CommandLine {
    ThreadFactory DEFAULT_THREAD_FACTORY = runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName("HeadlessMc-CommandLine");
        return thread;
    };

    void listen(HeadlessMc hmc) throws IOError;

    default void listenAsync(HeadlessMc hmc) {
        listenAsync(hmc, DEFAULT_THREAD_FACTORY);
    }

    default void listenAsync(HeadlessMc hmc, ThreadFactory factory) {
        // TODO: it would be useful to know which thread the command line is listening on?
        factory.newThread(() -> listen(hmc)).start();
    }

}
