package me.earth.headlessmc.api;

import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Getter
class HmcInstanceHolder {
    private final List<Consumer<HeadlessMc>> listeners = new CopyOnWriteArrayList<>();
    private volatile HeadlessMc instance;

    public synchronized void setInstance(HeadlessMc instance) {
        this.instance = instance;
        listeners.forEach(listener -> listener.accept(instance));
    }

    public synchronized void addListener(Consumer<HeadlessMc> listener) {
        if (this.instance != null) {
            listener.accept(this.instance);
        }

        listeners.add(listener);
    }

}
