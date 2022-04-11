package me.earth.headlessmc.command.line;

import me.earth.headlessmc.api.command.HasCommandContext;

import java.io.IOError;
import java.util.concurrent.ThreadFactory;

public interface Listener {
    ThreadFactory DEFAULT_THREAD_FACTORY = runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setName("HeadlessMc-CommandLine");
        return thread;
    };

    void listen(HasCommandContext context) throws IOError;

    default void listenAsync(HasCommandContext context) {
        listenAsync(context, DEFAULT_THREAD_FACTORY);
    }

    default void listenAsync(HasCommandContext context, ThreadFactory factory) {
        factory.newThread(() -> listen(context)).start();
    }

}
