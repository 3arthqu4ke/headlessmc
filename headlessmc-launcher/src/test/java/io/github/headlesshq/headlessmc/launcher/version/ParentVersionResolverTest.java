package io.github.headlesshq.headlessmc.launcher.version;

import lombok.SneakyThrows;
import lombok.val;
import io.github.headlesshq.headlessmc.launcher.UsesResources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class ParentVersionResolverTest implements UsesResources {
    @Test
    @SneakyThrows
    public void testParentVersionResolver() {
        val versions = new HashMap<String, Version>();
        val version1 = getVersion("version_child.json", 0);
        versions.put(version1.getName(), version1);
        val version2 = getVersion("version_parent.json", 1);
        versions.put(version2.getName(), version2);
        val version3 = getVersion("version_orphan.json", 2);
        versions.put(version2.getName(), version2);

        val parentVersionResolver = new ParentVersionResolver();
        parentVersionResolver.resolveParentVersions(versions);
        Assertions.assertTrue(versions.containsValue(version1));
        Assertions.assertTrue(versions.containsValue(version2));
        Assertions.assertFalse(versions.containsValue(version3));
    }

}
