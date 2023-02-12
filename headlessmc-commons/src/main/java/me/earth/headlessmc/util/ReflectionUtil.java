package me.earth.headlessmc.util;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Utility for Reflection.
 */
@UtilityClass
public class ReflectionUtil {
    /**
     * Iterates over the super classes of the given class.
     *
     * @param clazz  the class whose super classes will be iterated over.
     * @param action will be applied to all classes.
     */
    public static void iterate(Class<?> clazz, Consumer<Class<?>> action) {
        iterate(clazz, c -> {
            action.accept(c);
            return null;
        });
    }

    /**
     * Iterates over the super classes of the given class. Will stop iterating
     * if the given Function returns a value which is not {@code null} and
     * returns that value.
     *
     * @param clazz  the clazz whose super classes will be iterated over.
     * @param action the function every class is applied to.
     * @param <T>    the type of value returned by the given function.
     * @return the first value returned by the given function or {@code null}.
     */
    public static <T> T iterate(Class<?> clazz, Function<Class<?>, T> action) {
        T result = null;
        Class<?> current = clazz;
        while (current != null) {
            result = action.apply(current);
            if (result != null) {
                break;
            }

            current = current.getSuperclass();
        }

        return result;
    }

}
