package io.github.headlesshq.headlessmc.launcher.util;

import java.io.IOException;

@FunctionalInterface
public interface IOConsumer<T> {
    void accept(T value) throws IOException;

}
