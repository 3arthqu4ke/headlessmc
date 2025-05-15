package io.github.headlesshq.headlessmc.launcher.version.family;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class FamilyCleanerTest {
    @Test
    public void testFamilyCleaner() {
        val cleaner = new FamilyCleaner<HasParentImpl>();
        val family = FamilyUtilTest.setupFamily();
        val invalid = new HashSet<HasParentImpl>();
        invalid.add(family[3]);
        invalid.add(family[4]);
        // invalid.add(family[5]); we don't add the 5th,
        // but it should still get removed, because it's parent is on the list
        val list = new ArrayList<>(Arrays.asList(family));
        cleaner.clean(list, invalid);

        for (int i = 0; i < 3; i++) {
            Assertions.assertTrue(list.contains(family[i]));
        }

        for (int i = 3; i < family.length; i++) {
            Assertions.assertFalse(list.contains(family[i]));
        }
    }

}
