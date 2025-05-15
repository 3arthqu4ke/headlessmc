package io.github.headlesshq.headlessmc.runtime;

import lombok.CustomLog;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import io.github.headlesshq.headlessmc.api.config.HmcProperties;

/**
 * Run the HeadlessMc runtime without a Minecraft instance.
 */
@CustomLog
@UtilityClass
public class TestMain {
    @SneakyThrows
    public static void main(String[] args) {
        System.setProperty(HmcProperties.MAIN.getName(),
                           DummyMain.class.getName());
        Main.main(args);
    }

    public static final class DummyMain {
        @SneakyThrows
        @SuppressWarnings("unused")
        private static void main(String[] args) {
            log.info("Dummy main called!");
            Thread.sleep(Integer.MAX_VALUE);
        }
    }

}
