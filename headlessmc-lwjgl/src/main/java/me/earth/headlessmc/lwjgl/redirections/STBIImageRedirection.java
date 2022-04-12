package me.earth.headlessmc.lwjgl.redirections;

import me.earth.headlessmc.lwjgl.api.Redirection;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public enum STBIImageRedirection implements Redirection {
    INSTANCE;

    public static final String DESC =
        "Lorg/lwjgl/stb/STBImage;stbi_load_from_memory(" +
            "Ljava/nio/ByteBuffer;Ljava/nio/IntBuffer;" +
            "Ljava/nio/IntBuffer;Ljava/nio/IntBuffer;" +
            "I)Ljava/nio/ByteBuffer;";

    @Override
    public Object invoke(Object obj, String desc, Class<?> type, Object... args)
        throws Throwable {
        IntBuffer x = (IntBuffer) args[1];
        IntBuffer y = (IntBuffer) args[2];
        IntBuffer channels_in_file = (IntBuffer) args[3];
        int desired_channels = (int) args[4];

        x.rewind();
        y.rewind();

        ByteBuffer result = ByteBuffer.wrap(
            new byte[x.get(x.position()) * y.get(y.position())
                * (desired_channels != 0
                ? desired_channels
                : channels_in_file.get(channels_in_file.position()))]);

        x.put(1);
        y.put(1);

        return result;
    }

}
