package me.earth.headlessmc.runtime;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.api.HeadlessMcImpl;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.logging.LoggingService;
import me.earth.headlessmc.runtime.commands.RuntimeContext;

@UtilityClass
public class RuntimeApi {
    @Getter
    private static Runtime runtime;

    public static Runtime init(Config config, CommandLine input) {
        return init(config, Thread.currentThread(), input);
    }

    public static Runtime init(Config config, Thread mT, CommandLine input) {
        LoggingService loggingService = new LoggingService();
        loggingService.init();
        val hmc = new HeadlessMcImpl(() -> config, input, new ExitManager(), loggingService);
        val vm = new VM(config.get(RuntimeProperties.VM_SIZE, 128L).intValue());
        runtime = new Runtime(hmc, vm, mT);

        RuntimeContext context = new RuntimeContext(runtime);
        runtime.getCommandLine().setCommandContext(context);
        runtime.getCommandLine().setBaseContext(context);

        return runtime;
    }

}
