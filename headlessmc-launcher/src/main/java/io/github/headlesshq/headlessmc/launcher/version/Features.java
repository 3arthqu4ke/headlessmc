package io.github.headlesshq.headlessmc.launcher.version;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

// TODO: make this a config option
@RequiredArgsConstructor
public class Features {
    public static final Features EMPTY = new Features(Collections.emptyMap());
    private final Map<String, Boolean> features;

    public boolean getFeature(String name) {
        return features.getOrDefault(name, false);
    }

}
