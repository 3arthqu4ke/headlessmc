package me.earth.headlessmc.api.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

@Getter
@Setter
@AllArgsConstructor
public class Lazy<T> implements Supplier<T> {
    private final Object lock = new Object();
    private volatile Supplier<T> supplier;
    private volatile T value;

    @Override
    public T get() {
        if (value == null) {
            synchronized (lock) {
                if (value == null) {
                    value = supplier.get();
                }
            }
        }

        return value;
    }

}
