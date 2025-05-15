package io.github.headlesshq.headlessmc.launcher.version.family;

import java.util.Set;

/**
 * Represents the family of a {@link HasParent}.
 *
 * @param <T> the type of the members.
 */
// TODO: return the child at the bottom of the hierarchy?
public class Family<T extends HasParent<T>> {
    private final boolean circular;
    private final Set<T> members;

    public Family(Set<T> members, boolean circular) {
        this.circular = circular;
        this.members = members;
    }

    /**
     * @return {@code true}, if, at some point, a member has a parent
     * which is also a child of his.
     */
    public boolean isCircular() {
        return circular;
    }

    /**
     * @return the members of this family.
     */
    public Set<T> getMembers() {
        return members;
    }

}
