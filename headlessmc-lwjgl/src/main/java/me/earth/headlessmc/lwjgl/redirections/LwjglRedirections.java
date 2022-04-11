package me.earth.headlessmc.lwjgl.redirections;

import lombok.experimental.UtilityClass;
import me.earth.headlessmc.lwjgl.LwjglProperties;
import me.earth.headlessmc.lwjgl.api.RedirectionManager;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static me.earth.headlessmc.lwjgl.api.Redirection.of;

// TODO: redirect Keyboard and Mouse?
@UtilityClass
public class LwjglRedirections {
    public static final int TEXTURE_SIZE = Integer.parseInt(
        System.getProperty(LwjglProperties.TEXTURE_SIZE, "1024"));
    public static final boolean FULLSCREEN = Boolean.parseBoolean(
        System.getProperty(LwjglProperties.FULLSCREEN, "true"));
    public static final int SCREEN_WIDTH = Integer.parseInt(
        System.getProperty(LwjglProperties.SCREEN_WIDTH, "1920"));
    public static final int SCREEN_HEIGHT = Integer.parseInt(
        System.getProperty(LwjglProperties.SCREEN_HEIGHT, "1080"));
    public static final int REFRESH_RATE = Integer.parseInt(
        System.getProperty(LwjglProperties.REFRESH_RATE, "100"));
    public static final int BITS_PER_PIXEL = Integer.parseInt(
        System.getProperty(LwjglProperties.BITS_PER_PIXEL, "32"));
    public static final int JNI_VERSION = Integer.parseInt(
        System.getProperty(LwjglProperties.JNI_VERSION, "24"));

    public static void register(RedirectionManager manager) {
        manager.redirect("Lorg/lwjgl/opengl/Display;getWidth()I",
                         of(SCREEN_WIDTH));
        manager.redirect("Lorg/lwjgl/opengl/Display;getHeight()I",
                         of(SCREEN_HEIGHT));
        manager.redirect("Lorg/lwjgl/opengl/Display;isFullscreen()Z",
                         of(FULLSCREEN));


        manager.redirect("Lorg/lwjgl/DefaultSysImplementation;getJNIVersion()I",
                         of(JNI_VERSION));

        // TODO: make this configurable?
        manager.redirect("Lorg/lwjgl/opengl/Display;isActive()Z", of(true));

        manager.redirect("Lorg/lwjgl/opengl/DisplayMode;isFullscreenCapable()Z",
                         of(FULLSCREEN));
        manager.redirect("Lorg/lwjgl/opengl/DisplayMode;getWidth()I",
                         of(SCREEN_WIDTH));
        manager.redirect("Lorg/lwjgl/opengl/DisplayMode;getHeight()I",
                         of(SCREEN_HEIGHT));
        manager.redirect("Lorg/lwjgl/opengl/DisplayMode;getFrequency()I",
                         of(REFRESH_RATE));
        manager.redirect("Lorg/lwjgl/opengl/DisplayMode;getBitsPerPixel()I",
                         of(BITS_PER_PIXEL));

        manager.redirect(DisplayUpdater.DESC, new DisplayUpdater());

        manager.redirect("Lorg/lwjgl/glfw/GLFW;glfwInit()Z",
                         of(true));
        manager.redirect("Lorg/lwjgl/Sys;getVersion()Ljava/lang/String;",
                         of("HeadlessMc-Lwjgl"));

        manager.redirect("Lorg/lwjgl/Sys;getTimerResolution()J",
                         of(1000L));
        manager.redirect("Lorg/lwjgl/Sys;getTime()J", (obj, desc, type, args)
            -> System.nanoTime() / 1000000L);

        manager.redirect("Lorg/lwjgl/opengl/GL11;glGetTexLevelParameteri(III)I",
                         of(TEXTURE_SIZE));
        manager.redirect("Lorg/lwjgl/opengl/GL11;glGenLists(I)I", of(-1));

        manager.redirect(
            "proxy:Lorg/lwjgl/system/MemoryUtil$MemoryAllocator;malloc(J)J",
            of(1L));
        manager.redirect(
            "proxy:Lorg/lwjgl/system/MemoryUtil$MemoryAllocator;realloc(J)J",
            of(1L));
        manager.redirect(
            "proxy:Lorg/lwjgl/system/MemoryUtil$MemoryAllocator;calloc(J)J",
            of(1L));
        manager.redirect(
            "proxy:Lorg/lwjgl/system/MemoryUtil$MemoryAllocator;realloc(J)J",
            of(1L));

        // TODO: because MemoryUtil and the Buffers are actually being used,
        //  redirect all methods inside those to return proper Buffers?
        //  - ignore list?
        // I WISH WE COULD SUBCLASS BUFFERS WTF
        manager.redirect("Lorg/lwjgl/system/MemoryUtil;memByteBuffer(JI)" +
                             "Ljava/nio/ByteBuffer;",
                         of(ByteBuffer.wrap(
                             new byte[0])));
        manager.redirect(
            "Lorg/lwjgl/system/MemoryUtil;memAlloc(I)Ljava/nio/ByteBuffer;",
            of(ByteBuffer.wrap(new byte[0])));
        manager.redirect(
            "Lorg/lwjgl/system/MemoryStack;mallocInt(I)Ljava/nio/IntBuffer;",
            of(IntBuffer.wrap(new int[0])));

        manager.redirect("Lorg/lwjgl/BufferUtils;createIntBuffer(I)" +
                             "Ljava/nio/IntBuffer;",
                         (obj, desc, type, args) -> IntBuffer.wrap(
                             new int[(int) args[0]]));
        manager.redirect("Lorg/lwjgl/BufferUtils;createFloatBuffer(I)" +
                             "Ljava/nio/FloatBuffer;",
                         (obj, desc, type, args) -> FloatBuffer.wrap(
                             new float[(int) args[0]]));
        manager.redirect("Lorg/lwjgl/system/MemoryUtil;createIntBuffer(I)" +
                             "Ljava/nio/IntBuffer;",
                         (obj, desc, type, args) -> IntBuffer.wrap(
                             new int[(int) args[0]]));
    }

}
