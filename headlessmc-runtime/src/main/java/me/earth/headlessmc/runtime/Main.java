package me.earth.headlessmc.runtime;

import lombok.CustomLog;
import lombok.experimental.UtilityClass;
import lombok.val;
import me.earth.headlessmc.api.command.line.CommandLineManager;
import me.earth.headlessmc.api.config.ConfigImpl;
import me.earth.headlessmc.api.config.HmcProperties;

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

        val in = new CommandLineManager();
        val runtime = RuntimeApi.init(config, in);
        log.info("Initializing Runtime...");
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
