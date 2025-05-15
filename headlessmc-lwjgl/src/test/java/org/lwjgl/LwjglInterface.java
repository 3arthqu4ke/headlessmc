package org.lwjgl;

import io.github.headlesshq.headlessmc.lwjgl.LwjglInstrumentationTest;

/**
 * {@link LwjglInstrumentationTest}
 */
@SuppressWarnings("unused")
public interface LwjglInterface {
    static LwjglInterface factoryMethod(String dontCall) {
        return null;
    }

    void abstractMethod();

    default int someMethod() {
        return 0;
    }

}
