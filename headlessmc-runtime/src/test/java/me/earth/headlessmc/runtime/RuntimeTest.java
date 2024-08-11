package me.earth.headlessmc.runtime;

import me.earth.headlessmc.api.HeadlessMcApi;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.runtime.reflection.RuntimeReflection;

public interface RuntimeTest {
    default RuntimeReflection getRuntime() {
        System.setProperty(RuntimeProperties.ENABLE_REFLECTION.getName(), "true");
        new RuntimeInitializer().init(ConfigImpl.empty());
        return (RuntimeReflection) HeadlessMcApi.getInstance();
    }

}
