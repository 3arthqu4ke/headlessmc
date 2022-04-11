package me.earth.headlessmc.runtime.util;

@FunctionalInterface
public interface ThrowingFunction<IN, OUT, EXCEPTION extends Throwable> {
    OUT apply(IN in) throws EXCEPTION;

}
