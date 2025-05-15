package io.github.headlesshq.headlessmc.lwjgl.redirections.stb;

/**
 * TODO: is the STB library unsafe? couldn't we just load it?
 */
public class STBImage {
    public static final String DESC =
            "Lorg/lwjgl/stb/STBImage;stbi_load_from_memory(" +
            "Ljava/nio/ByteBuffer;Ljava/nio/IntBuffer;" +
            "Ljava/nio/IntBuffer;Ljava/nio/IntBuffer;" +
            "I)Ljava/nio/ByteBuffer;";

}
