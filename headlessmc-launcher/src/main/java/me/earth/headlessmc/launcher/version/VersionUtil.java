package me.earth.headlessmc.launcher.version;

import lombok.experimental.UtilityClass;
import me.earth.headlessmc.api.HasName;
import me.earth.headlessmc.util.Table;

import java.util.Collection;
import java.util.stream.Collectors;

@UtilityClass
public final class VersionUtil {
    public static Collection<Version> releases(Collection<Version> versions) {
        return versions.stream()
                       .filter(v -> "release".equalsIgnoreCase(v.getType()))
                       .collect(Collectors.toList());
    }

    public static String makeTable(Collection<Version> versions) {
        return new Table<Version>()
            .withColumn("id", v -> String.valueOf(v.getId()))
            .withColumn("name", HasName::getName)
            .withColumn("parent", v -> v.getParent() == null
                ? "" : v.getParent().getName())
            .addAll(versions)
            .build();
    }

}
