package me.earth.headlessmc.launcher.launch;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.headlessmc.launcher.version.Version;

@RequiredArgsConstructor
class DelegatingVersion implements Version {
    @Delegate
    protected final Version version;

}
