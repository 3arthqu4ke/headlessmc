package me.earth.headlessmc.lwjgl.redirections;

import me.earth.headlessmc.lwjgl.api.RedirectionManager;

import java.nio.ByteBuffer;

/**
 * Redirections for memoryReallocation, those methods of {@code org.lwjgl.system.MemoryUtil} that make ByteBuffers larger.
 */
public class MemReallocRedirections {
    public static void redirect(RedirectionManager manager) {
        manager.redirect("Lorg/lwjgl/system/MemoryUtil;memRealloc" +
                             "(Ljava/nio/ByteBuffer;I)Ljava/nio/ByteBuffer;",
                         (obj, desc, type, args) -> memRealloc((ByteBuffer) args[0], (Integer) args[1]));
    }

    static ByteBuffer memRealloc(ByteBuffer byteBuffer, int size) {
        int position = byteBuffer.position();
        byte[] array = new byte[size];
        ByteBuffer result = ByteBuffer.wrap(array);
        // sadly get with index is java 13+ so we have to rewind to fill our array from index 0
        // TODO: is there a better way? ByteBuffer should almost always be backed by array in this case?
        byteBuffer.rewind();
        byteBuffer.get(array, 0, Math.min(byteBuffer.remaining(), size));
        byteBuffer.position(position);
        result.position(position);
        return result;
    }

}
