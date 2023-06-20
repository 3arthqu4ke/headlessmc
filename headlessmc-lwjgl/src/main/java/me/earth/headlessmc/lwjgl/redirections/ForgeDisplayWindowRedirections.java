package me.earth.headlessmc.lwjgl.redirections;

import me.earth.headlessmc.lwjgl.api.RedirectionManager;

import java.nio.ByteBuffer;

import static me.earth.headlessmc.lwjgl.api.Redirection.of;

/**
 * Forge 1.20.1 introduces the
 * {@code net.minecraftforge.fml.earlydisplay.DisplayWindow} class.
 */
public class ForgeDisplayWindowRedirections {
    public static void redirect(RedirectionManager manager) {
        manager.redirect("Lorg/lwjgl/glfw/GLFW;glfwGetPrimaryMonitor()J",
                         of(1L));

        manager.redirect("Lorg/lwjgl/glfw/GLFW;" +
                             "glfwCreateWindow(IILjava/lang/CharSequence;JJ)J",
                         of(1L));

        // act like we compiled shader
        manager.redirect("Lorg/lwjgl/opengl/GL20C;glGetShaderi(II)I", of(1));
        manager.redirect("Lorg/lwjgl/opengl/GL20C;glGetProgrami(II)I", of(1));

        manager.redirect("Lorg/lwjgl/stb/STBTruetype;" +
                             "stbtt_InitFont(Lorg/lwjgl/stb/STBTTFontinfo;" +
                             "Ljava/nio/ByteBuffer;)Z", of(true));

        manager.redirect("Lorg/lwjgl/opengl/GL30C;glMapBufferRange(IJJI)" +
                             "Ljava/nio/ByteBuffer;",
                         (obj, desc, type, args) ->
                             ByteBuffer.wrap(new byte[(int) ((long) args[2])]));
    }

}
