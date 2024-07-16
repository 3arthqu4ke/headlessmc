package me.earth.headlessmc.launcher.version.family;

import lombok.experimental.UtilityClass;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Util for {@link HasParent}.
 */
@UtilityClass
public class FamilyUtil {
    public static <T extends HasParent<T>> T getOldestParent(T child) {
        AtomicReference<T> reference = new AtomicReference<>();
        iterate(child, reference::set);
        return reference.get();
    }

    /**
     * Creates an {@link Iterable} allowing you to iterate over the parents of
     * the given child in reverse order, starting with the parent which has no
     * parent. If the family of the given child is circular this method will
     * throw an exception.
     *
     * @param child the child to start with.
     * @param <T>   the type of the family to iterate over.
     * @return an Iterable.
     */
    public static <T extends HasParent<T>> Iterable<T> reverse(T child) {
        Deque<T> stack = new ArrayDeque<>();
        iterateParents(child, parent -> {
            stack.push(parent);
            return null;
        });

        return stack;
    }

    /**
     * Finds and set the parents for all {@link HasParent}s in the given
     * Iterable.
     *
     * @param family       the child's to find parents for.
     * @param parentFinder a function finding a parent for each child. Should
     *                     return {@code null} if the child has no parents.
     * @param <T>          the type of the HasParents.
     */
    public static <T extends HasParent<T>> void resolveParents(
        Iterable<T> family,
        Function<T, T> parentFinder
    ) {
        for (T child : family) {
            T parent = parentFinder.apply(child);
            if (parent != null) {
                child.setParent(parent);
            }
        }
    }

    public static <T extends HasParent<T>> void iterate(
        T child,
        Consumer<T> action) {
        iterateParents(child, () -> null, t -> {
            action.accept(t);
            return null;
        });
    }

    public static <T extends HasParent<T>> void iterateTopDown(
        T child,
        Consumer<T> action) {
        reverse(child).forEach(action);
    }

    /**
     * Calls {@link FamilyUtil#iterateParents(HasParent, Supplier, Function)}
     * for the defaultResult <tt>() -> null</tt>.
     */
    public static <T extends HasParent<T>, S> S iterateParents(
        T child,
        Function<T, S> action) {
        return iterateParents(child, () -> null, action);
    }

    /**
     * Iterates over the child's ancestry. It is important that the child's
     * {@link Family} which can be obtained via {@link FamilyUtil#getFamily(HasParent)}
     * is not circular, or that you check for circularity yourself!
     *
     * @param child         the child whose ancestors to iterate over.
     * @param defaultResult supplies the default result to return when none of
     *                      the calls to the given function returned a value
     *                      that was not {@code null}. This is useful to
     *                      prevent problems with unboxing etc.
     * @param action        action to perform for every ancestor. Should return
     *                      a value which is not {@code null} if you wish to
     *                      stop iterating. Will be called for the given child
     *                      first.
     * @param <T>           the type of the child and it's ancestors.
     * @return {@code true} if the action returned {@code true}, otherwise
     * {@code false}.
     */
    public static <T extends HasParent<T>, S> S iterateParents(
        T child,
        Supplier<S> defaultResult,
        Function<T, S> action) {
        T parent = child;
        while (parent != null) {
            S result = action.apply(parent);
            if (result != null) {
                return result;
            }

            parent = parent.getParent();
        }

        return defaultResult.get();
    }

    /**
     * Forms a {@link Family} object from the child' ancestry.
     *
     * @param child the child whose family to analyze.
     * @param <T>   the type of the Families Members.
     * @return a Family representing the child's ancestry.
     */
    public static <T extends HasParent<T>> Family<T> getFamily(T child) {
        Set<T> mem = new HashSet<>();
        boolean circular = iterateParents(child,
                                          () -> false,
                                          c -> mem.add(c) ? null : true);
        return new Family<>(mem, circular);
    }

}
