package io.github.headlesshq.headlessmc.launcher.command;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class VersionTypeFilterTest {
    @Test
    public void testVersionTypeFilter() {
        val filter = new VersionTypeFilter<>(Function.identity());
        val list = asList("test", "release", "snapshot");

        Assertions.assertEquals(3, filter.apply(list).size());
        Assertions.assertEquals(list, filter.apply(list));

        Assertions.assertEquals(asList("test", "snapshot"),
                                filter.apply(list, "-release"));
        Assertions.assertEquals(asList("test", "release"),
                                filter.apply(list, "-snapshot"));
        Assertions.assertEquals(asList("release", "snapshot"),
                                filter.apply(list, "-other"));
        Assertions.assertEquals(singletonList("test"),
                                filter.apply(list, "-release", "-snapshot"));
        Assertions.assertEquals(singletonList("snapshot"),
                                filter.apply(list, "-release", "-other"));
        Assertions.assertEquals(singletonList("release"),
                                filter.apply(list, "-snapshot", "-other"));
        Assertions.assertEquals(emptyList(),
                                filter.apply(list, "-release", "-snapshot",
                                             "-other"));
    }

}
