package io.github.headlesshq.headlessmc.launcher.version;


import lombok.Getter;
import io.github.headlesshq.headlessmc.launcher.launch.DelegatingVersion;

@Getter
public class DummyVersion extends DelegatingVersion {
    private final String name;
    private final Version parent;

    public DummyVersion(String name, Version parent) {
        super(null);
        this.name = name;
        this.parent = parent;
    }

}
