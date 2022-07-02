package me.earth.headlessmc.launcher.version.family;

/**
 * A type which has a parent of a certain type.
 *
 * @param <T> the type of the parent.
 */
public interface HasParent<T> {
    /**
     * @return the parent for this object or <tt>null</tt> if it has no parents.
     */
    T getParent();

    // TODO: maybe move the mutability into a separate interface?

    /**
     * Sets the parent for this object. The given parent will now be returned by
     * {@link HasParent#getParent()}.
     *
     * @param parent the parent for this object.
     */
    void setParent(T parent);

}
