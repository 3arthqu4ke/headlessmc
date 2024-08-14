package me.earth.headlessmc.lwjgl.redirections.stb;

import me.earth.headlessmc.lwjgl.api.Redirection;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * On some platforms (Android) java.awt might not be available.
 * TODO: use a library instead
 */
public enum STBImageRedirectionNoAWT implements Redirection {
    INSTANCE;

    @Override
    public Object invoke(Object obj, String desc, Class<?> type, Object... args) throws Throwable {
        // ByteBuffer buffer = (ByteBuffer) args[0];
        // TODO: read this with something else than AWT?
        IntBuffer x = (IntBuffer) args[1];
        IntBuffer y = (IntBuffer) args[2];
        IntBuffer channelsInFile = (IntBuffer) args[3];
        int desired_channels = (int) args[4];

        ByteBuffer result = ByteBuffer.wrap(
                new byte[x.get(x.position()) * y.get(y.position())
                        * (desired_channels != 0
                        ? desired_channels
                        : channelsInFile.get(channelsInFile.position()))]);

        x.put(0, 128);
        y.put(0, 128);
        channelsInFile.put(0, desired_channels);
        return result;
    }

}
