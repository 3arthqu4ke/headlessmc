package org.lwjgl;

import io.github.headlesshq.headlessmc.lwjgl.LwjglInstrumentationTest;

/**
 * {@link LwjglInstrumentationTest}
 */
@SuppressWarnings("unused")
public class Lwjgl {
    public final String string;

    public Lwjgl(Object string) {
        this.string = (String) string;
    }

    public static Lwjgl factoryMethod(String dontCall) {
        return null;
    }

    public byte[] returnsByteArray(String dummy) {
        return null;
    }

    public int[][] returns2dIntArray(String dummy) {
        return null;
    }

    public String someMethod() {
        return null;
    }

    public short someShortMethod() {
        return (short) 0;
    }

}
