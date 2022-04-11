package me.earth.headlessmc.runtime;

import dev.xdark.deencapsulation.Deencapsulation;
import lombok.CustomLog;

@CustomLog
public class Deencapsulator {
    public void deencapsulate(Class<?> clazz) {
        log.debug("Deencapsulating: " + clazz.getName());
        Deencapsulation.deencapsulate(clazz);
    }

}
