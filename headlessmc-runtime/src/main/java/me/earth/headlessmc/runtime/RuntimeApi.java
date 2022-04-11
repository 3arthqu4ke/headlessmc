package me.earth.headlessmc.runtime;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.HeadlessMcImpl;
import me.earth.headlessmc.api.PasswordAware;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.logging.SimpleLog;
import me.earth.headlessmc.runtime.commands.RuntimeContext;

@UtilityClass
public class RuntimeApi {
    @Getter
    private static Runtime runtime;

    public static Runtime init(Config config, PasswordAware input) {
        return init(config, Thread.currentThread(), input);
    }

    public static Runtime init(Config config, Thread mT, PasswordAware input) {
        val hmc = new HeadlessMcImpl(new SimpleLog(), () -> config, input);
        val vm = new VM(config.get(RuntimeProperties.VM_SIZE, 128L).intValue());
        runtime = new Runtime(hmc, vm, mT);
        runtime.setCommandContext(new RuntimeContext(runtime));
        return runtime;
    }

}
