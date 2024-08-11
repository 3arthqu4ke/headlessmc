package me.earth.headlessmc.api.classloading;

import lombok.CustomLog;
import lombok.Setter;

import java.lang.reflect.Method;

/**
 * Wrapper around dev.xdark.deencapsulation.Deencapsulation,
 * which might already fail in the static initializer on Java 8,
 * so it must be handled with care.
 *
 * @see <a href=https://github.com/xxDark/deencapsulation>https://github.com/xxDark/deencapsulation</a>
 */
@Setter
@CustomLog
public class Deencapsulator {
    /**
     * Allows you to access a Class beyond module boundaries.
     *
     * @param clazz the clazz to deencapsulate.
     * @see <a href=https://github.com/xxDark/deencapsulation>https://github.com/xxDark/deencapsulation</a>
     */
    public void deencapsulate(Class<?> clazz) {
        try {
            Class<?> deencapsulation = Class.forName("dev.xdark.deencapsulation.Deencapsulation");
            Method deencapsulate = deencapsulation.getMethod("deencapsulate", Class.class);
            deencapsulate.invoke(null, clazz);
        } catch (Throwable t) {
            log.debug("Failed to deencapsulate " + clazz, t);
        }
    }

}
