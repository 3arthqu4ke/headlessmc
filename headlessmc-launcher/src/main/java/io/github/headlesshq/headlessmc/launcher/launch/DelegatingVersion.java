package io.github.headlesshq.headlessmc.launcher.launch;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import io.github.headlesshq.headlessmc.launcher.version.Version;

@RequiredArgsConstructor
public class DelegatingVersion implements Version {
    @Delegate
    protected final Version version;

}
