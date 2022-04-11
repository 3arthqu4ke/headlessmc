package me.earth.headlessmc.launcher.util;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class CollectionUtil {
    public static <K, V> Map<K, V> mapOfSize(int amountOfElements) {
        return new HashMap<>((int) (amountOfElements / 0.75 + 1));
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }

        return null;
    }

}
