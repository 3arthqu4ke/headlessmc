package me.earth.headlessmc.launcher;

import lombok.experimental.Delegate;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public abstract class Service<T> implements Refreshable, Collection<T> {
    @Delegate
    protected Collection<T> contents = Collections.emptyList();

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
