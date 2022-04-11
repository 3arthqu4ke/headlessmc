package me.earth.headlessmc.runtime.mapping;

import java.io.File;

public class MappingFactory {
    public Mapping create() {
        return NoMapping.INSTANCE;
    }

}
