package me.earth.headlessmc.launcher;

import lombok.experimental.Delegate;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Service<T> implements Refreshable, Collection<T> {
    @Delegate                          // not empty list for testing purposes
    protected Collection<T> contents = new ArrayList<>(0);

    protected abstract Collection<T> update();

    @Override
    public void refresh() {
        contents = update();
    }

    public static <S, T extends Service<S>> T refresh(T service) {
        service.refresh();
        return service;
    }

}
