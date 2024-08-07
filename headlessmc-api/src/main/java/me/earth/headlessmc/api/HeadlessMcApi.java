package me.earth.headlessmc.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Provides a global instance of {@link HeadlessMc}.
 */
@Getter
@Setter
public class HeadlessMcApi {
    /**
     * A global instance of {@link HeadlessMc}.
     */
    private static HeadlessMc headlessMc;

}
