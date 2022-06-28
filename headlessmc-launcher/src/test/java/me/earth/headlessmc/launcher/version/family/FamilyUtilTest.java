package me.earth.headlessmc.launcher.version.family;

import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class FamilyUtilTest {
    @Test
    public void testGetOldestParent() {
        val family = setupFamily();
        val oldest = FamilyUtil.getOldestParent(family[2]);
        Assertions.assertEquals(family[0], oldest);
    }

    @Test
    public void testReverse() {
        val family = setupFamily();
        val reversed = FamilyUtil.reverse(family[2]);
        val itr = reversed.iterator();

        Assertions.assertTrue(itr.hasNext());
        Assertions.assertEquals(family[0], itr.next());

        Assertions.assertTrue(itr.hasNext());
        Assertions.assertEquals(family[1], itr.next());

        Assertions.assertTrue(itr.hasNext());
        Assertions.assertEquals(family[2], itr.next());

        Assertions.assertFalse(itr.hasNext());
    }

    @Test
    public void testResolveParents() {
        val family = setupFamily();
        val parent = new HasParentImpl();
        FamilyUtil.resolveParents(Arrays.asList(family), child -> parent);
        Arrays.stream(family)
              .forEach(c -> Assertions.assertEquals(parent, c.getParent()));
    }

    @Test
    public void testIterate() {
        val family = setupFamily();
        val index = new int[]{0};
        FamilyUtil.iterate(
            family[2], c -> Assertions.assertEquals(family[2 - index[0]++], c));
        Assertions.assertEquals(3, index[0]);
    }

    @Test
    public void testIterateParents() {
        val family = setupFamily();
        val index = new int[]{0};
        val defaultResult = new Object();
        var result = FamilyUtil.iterateParents(
            family[2],
            () -> defaultResult,
            c -> {
                Assertions.assertEquals(family[2 - index[0]++], c);
                return null;
            });
        Assertions.assertEquals(defaultResult, result);
        Assertions.assertEquals(3, index[0]);

        val value = new Object();
        index[0] = 0;
        result = FamilyUtil.iterateParents(family[2], c -> {
            Assertions.assertEquals(family[2 - index[0]++], c);
            return value;
        });
        Assertions.assertEquals(value, result);
        Assertions.assertEquals(1, index[0]);
    }

    @Test
    public void testGetFamily() {
        val array = setupFamily();
        var family = FamilyUtil.getFamily(array[2]);
        Assertions.assertFalse(family.isCircular());
        Assertions.assertEquals(3, family.getMembers().size());
        for (int i = 0; i < 3; i++) {
            Assertions.assertTrue(family.getMembers().contains(array[i]));
        }

        family = FamilyUtil.getFamily(array[3]);
        Assertions.assertTrue(family.isCircular());
        Assertions.assertEquals(2, family.getMembers().size());
        Assertions.assertTrue(family.getMembers().contains(array[3]));
        Assertions.assertTrue(family.getMembers().contains(array[4]));
        Assertions.assertFalse(family.getMembers().contains(array[5]));
    }

    public static HasParentImpl[] setupFamily() {
        val member1 = new HasParentImpl();
        val member2 = new HasParentImpl(member1);
        val member3 = new HasParentImpl(member2);

        val invalidMember1 = new HasParentImpl();
        val invalidMember2 = new HasParentImpl(invalidMember1);
        invalidMember1.setParent(invalidMember2);

        val invalidMember3 = new HasParentImpl(invalidMember1);

        return new HasParentImpl[]{member1, member2, member3,
            invalidMember1, invalidMember2, invalidMember3};
    }

}
