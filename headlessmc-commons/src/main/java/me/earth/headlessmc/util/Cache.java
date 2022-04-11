package me.earth.headlessmc.util;

import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Caches a value supplied by a given {@link Supplier}.
 *
 * @param <T> the type of the value cached by this cache.
 */
@RequiredArgsConstructor
public class Cache<T> implements Supplier<T> {
    private final Object lock = new Object();
    private final Supplier<T> supplier;
    private volatile T value;

    /**
     * @return the value supplied by the {@link Supplier} for this cache or
     * <tt>null</tt>.
     */
    @Override
    public T get() {
        return isPresent() ? value : null;
    }

    /**
     * @return <tt>false</tt> if a call to {@link Cache#get()} will return
     * <tt>null</tt>, <tt>true</tt> otherwise.
     */
    public boolean isPresent() {
        if (value == null) {
            synchronized (lock) {
                if (value == null) {
                    value = supplier.get();
                    return value != null;
                }
            }
        }

        return true;
    }

    /**
     * Makes the given {@link Consumer} accept the value supplied by {@link
     * Cache#get()} if it's not <tt>null</tt>.
     *
     * @param consumer the consumer accepting the value.
     * @return <tt>true</tt> if the given consumer has been used.
     */
    public boolean ifPresent(Consumer<T> consumer) {
        T value = get();
        if (value != null) {
            consumer.accept(value);
            return true;
        }

        return false;
    }

    /**
     * Returns the value returned by the given function if it's applied to the
     * value returned by {@link Cache#get()} or <tt>null</tt> if the value for
     * this cache is not present.
     *
     * @param function the function returning the result.
     * @param <V>      the type of the result
     * @return the result of {@link Function#apply(Object)} or <tt>null</tt>.
     */
    public <V> V returnIfPresent(Function<T, V> function) {
        T value = get();
        return value == null ? null : function.apply(value);
    }

}
