package me.earth.headlessmc.lwjgl.lwjgltestclasses;

/**
 * {@link me.earth.headlessmc.lwjgl.LwjglInstrumentationTest}
 */
@SuppressWarnings("unused")
public class Lwjgl {
    public final String string;

    public Lwjgl(Object string) {
        this.string = (String) string;
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

    public static Lwjgl factoryMethod(String dontCall) {
        return null;
    }

}
