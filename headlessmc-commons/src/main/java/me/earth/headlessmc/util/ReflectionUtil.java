package me.earth.headlessmc.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
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
     * if the given Function returns a value which is not <tt>null</tt> and
     * returns that value.
     *
     * @param clazz  the clazz whose super classes will be iterated over.
     * @param action the function every class is applied to.
     * @param <T>    the type of value returned by the given function.
     * @return the first value returned by the given function or <tt>null</tt>.
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

    /**
     * Goes through every field in the objects class and its super classes,
     * accesses the objects assigned to them and returns a list of those
     * objects. Objects which are <tt>null</tt> will be skipped and {@link
     * IllegalAccessException}s will be swallowed.
     *
     * @param object  the object whose fields to collect.
     * @param type fields of this type will be collected.
     * @param <T>  the type of the field to collect.
     * @return a list of objects behind the fields in the given object.
     */
    public static <T> List<T> collect(Object object, Class<T> type) {
        // TODO: is this actually being used?
        List<T> result = new ArrayList<>();
        iterate(object.getClass(), clazz -> {
            for (Field field : clazz.getDeclaredFields()) {
                if (type.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    try {
                        Object obj = field.get(object);
                        if (obj != null) {
                            result.add(type.cast(obj));
                        }
                    } catch (IllegalAccessException e) {
                        // should we throw this instead?
                        e.printStackTrace();
                    }
                }
            }
        });

        return result;
    }

}
