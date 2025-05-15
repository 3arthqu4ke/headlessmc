package org.lwjgl;

import io.github.headlesshq.headlessmc.lwjgl.LwjglInstrumentationTest;

import java.nio.ByteBuffer;

/**
 * {@link LwjglInstrumentationTest}
 */
@SuppressWarnings("unused")
public abstract class AbstractLwjglClass {
    public static ByteBuffer returnsAbstractByteBuffer(String arg) {
        return ByteBuffer.wrap(new byte[0]);
    }

    public static AbstractLwjglClass factoryMethod(String dontCall) {
        return null;
    }

    public abstract void abstractMethod();

    public long someMethod() {
        return 100L;
    }

}
