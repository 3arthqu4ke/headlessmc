package me.earth.headlessmc.runtime;

import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.api.HeadlessMcApi;
import me.earth.headlessmc.api.HeadlessMcImpl;
import me.earth.headlessmc.api.classloading.ApiClassloadingHelper;
import me.earth.headlessmc.api.command.line.CommandLine;
import me.earth.headlessmc.api.config.Config;
import me.earth.headlessmc.api.config.HmcProperties;
import me.earth.headlessmc.api.exit.ExitManager;
import me.earth.headlessmc.jline.JLineCommandLineReader;
import me.earth.headlessmc.jline.JLineProperties;
import me.earth.headlessmc.logging.LoggingService;
import me.earth.headlessmc.runtime.commands.RuntimeContext;

import java.util.logging.Level;

/**
 * Gives you a Framework to construct your own {@link HeadlessMc} instances.
 */
public class RuntimeInitializer {
    public void init(Config config) {
        LoggingService loggingService = new LoggingService();
        loggingService.setFileHandler(config.get(HmcProperties.FILE_HANDLER_ENABLED, false)); // usually no FileHandler at runtime
        loggingService.init();
        loggingService.setLevel(Level.INFO);

        HeadlessMc hmc = instance(config, loggingService);
        createCommandContext(hmc);
        readCommandLine(hmc);
    }

    protected HeadlessMc instance(Config config, LoggingService loggingService) {
        HeadlessMc hmc = new HeadlessMcImpl(() -> config, new CommandLine(), new ExitManager(), loggingService);
        HeadlessMcApi.setInstance(hmc);
        return hmc;
    }

    protected void createCommandContext(HeadlessMc hmc) {
        RuntimeContext context = new RuntimeContext(hmc);
        hmc.getCommandLine().setCommandContext(context);
        hmc.getCommandLine().setBaseContext(context);
    }

    protected void readCommandLine(HeadlessMc hmc) {
        if (ApiClassloadingHelper.installOnOtherInstances(hmc) == null) {
            if (hmc.getConfig().get(JLineProperties.ENABLED, true)) {
                // TODO: see hmc-specifics, fail early if JLineCommandLineReader fails?!
                hmc.getCommandLine().setCommandLineProvider(JLineCommandLineReader::new);
            }

            hmc.getCommandLine().readAsync(hmc);
        }
    }

}
