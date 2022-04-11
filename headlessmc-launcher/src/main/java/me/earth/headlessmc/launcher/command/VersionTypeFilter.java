package me.earth.headlessmc.launcher.command;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import me.earth.headlessmc.command.CommandUtil;
import me.earth.headlessmc.launcher.version.Version;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VersionTypeFilter<T> {
    private final Function<T, String> typeGetter;

    public Collection<T> apply(Collection<T> collection, String... args) {
        var r = collection;
        r = filter("-release", r, t -> !"release".equalsIgnoreCase(t), args);
        r = filter("-snapshot", r, t -> !"snapshot".equalsIgnoreCase(t), args);
        r = filter("-other", r, t -> "snapshot".equalsIgnoreCase(t)
            || "release".equalsIgnoreCase(t), args);
        return r;
    }

    private Collection<T> filter(String flag, Collection<T> coll,
                                 Predicate<String> type, String... args) {
        if (CommandUtil.hasFlag(flag, args)) {
            return coll.stream()
                       .filter(t -> type.test(typeGetter.apply(t)))
                       .collect(Collectors.toList());
        }

        return coll;
    }

    public static VersionTypeFilter<Version> forVersions() {
        return new VersionTypeFilter<>(Version::getType);
    }

    public static Map<String, String> getArgs() {
        val result = new HashMap<String, String>();
        result.put("-release", "Only displays versions of type 'release'.");
        result.put("-snapshot", "Only displays versions of type 'snapshot'.");
        result.put("-other", "Only displays versions of unknown type.");
        return result;
    }

}
