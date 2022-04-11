package me.earth.headlessmc.api;

/**
 * A type which can be named.
 */
public interface HasName {
    /**
     * @return the name of this object.
     */
    String getName();

    static <T extends HasName> T getByName(String name, Iterable<T> nameables) {
        for (T t : nameables) {
            if (name.equals(t.getName())) {
                return t;
            }
        }

        return null;
    }

}
