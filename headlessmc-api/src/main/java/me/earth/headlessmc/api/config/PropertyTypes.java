package me.earth.headlessmc.api.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import me.earth.headlessmc.api.config.Property;

import java.util.function.Function;

@UtilityClass
public class PropertyTypes {
    public static Property<String> string(String name) {
        return new PropertyImpl<>(name, Function.identity());
    }

    public static Property<String[]> array(String name, String delimiter) {
        return new PropertyImpl<>(name, nullable(value -> {
            if (value.isEmpty()) {
                return null;
            }

            return value.split(delimiter);
        }));
    }

    public static Property<Long> number(String name) {
        return new PropertyImpl<>(name, nullable(value -> {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException numberFormatException) {
                throw new NumberFormatException(String.format(
                    "Couldn't parse Property %s, %s is not a number!",
                    name, value));
            }
        }));
    }

    public static Property<Boolean> bool(String name) {
        return new PropertyImpl<>(name, nullable(Boolean::parseBoolean));
    }

    public static <T extends Enum<T>> Property<T> constant(String name,
                                                           Class<T> type) {
        return new PropertyImpl<>(name, nullable(value -> {
            for (T t : type.getEnumConstants()) {
                if (t.name().equalsIgnoreCase(value)) {
                    return t;
                }
            }

            return null;
        }));
    }

    private static <T> Function<String, T> nullable(Function<String, T> func) {
        return value -> {
            if (value == null) {
                return null;
            }

            return func.apply(value);
        };
    }

    @RequiredArgsConstructor
    private static final class PropertyImpl<T> implements Property<T> {
        @Getter
        private final String name;
        private final Function<String, T> function;

        @Override
        public T parse(String value) {
            return function.apply(value);
        }
    }

}
