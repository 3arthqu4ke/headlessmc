package me.earth.headlessmc.runtime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import me.earth.headlessmc.api.HeadlessMc;

// TODO: use mappings to get the real names of classes fields and methods
// TODO: honestly who's ever going to use the VM and reflection commands?
//  We could load the right Runtime with a ServiceLoader and just a QuitCommand
//  for the default implementation would be fine
@RequiredArgsConstructor
public class Runtime implements HeadlessMc {
    @Delegate
    private final HeadlessMc headlessMc;
    @Getter
    private final VM vm;
    @Getter
    private final Thread mainThread;

}
