package me.earth.headlessmc.runtime;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.command.line.CommandLineImpl;
import me.earth.headlessmc.config.ConfigImpl;
import me.earth.headlessmc.config.HmcProperties;
import me.earth.headlessmc.logging.LoggingHandler;

@CustomLog
@UtilityClass
public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: detect if we are running in Memory!!!
        //  Otherwise we read the command line unnecessarily often

        val config = ConfigImpl.empty();
        val mainClassName = config.get(HmcProperties.MAIN);
        if (mainClassName == null) {
            throw new IllegalStateException(
                "Property '" + HmcProperties.MAIN.getName()
                    + "' was null, can't call mainClass!");
        }

        LoggingHandler.apply();
        log.info("Initializing Runtime...");
        val in = new CommandLineImpl();
        val runtime = RuntimeApi.init(config, in);
        in.listenAsync(runtime);

        log.info("Getting MainClass: " + mainClassName);
        val mainClass = Class.forName(config.get(HmcProperties.MAIN));
        if (config.get(HmcProperties.DEENCAPSULATE, false)) {
            new Deencapsulator().deencapsulate(mainClass);
        }

        log.info("Calling main method: " + mainClassName);
        val main = mainClass.getDeclaredMethod("main", String[].class);
        main.setAccessible(true);
        main.invoke(null, (Object) args);
    }

}
