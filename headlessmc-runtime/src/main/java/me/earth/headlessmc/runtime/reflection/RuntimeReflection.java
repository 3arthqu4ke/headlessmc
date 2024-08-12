package me.earth.headlessmc.runtime.reflection;

import lombok.Data;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.HeadlessMc;

// TODO: array command?
// I am not fond of this, the reflection commands were a bad idea and should not be used!
// @Deprecated basically...
@Data
public class RuntimeReflection implements HeadlessMc {
    @Delegate
    private final HeadlessMc headlessMc;
    private final Thread mainThread;
    private final VM vm;

}
