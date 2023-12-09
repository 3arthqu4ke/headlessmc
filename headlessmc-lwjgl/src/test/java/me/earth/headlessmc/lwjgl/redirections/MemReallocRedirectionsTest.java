package me.earth.headlessmc.lwjgl.redirections;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemReallocRedirectionsTest {
    @Test
    public void testMemRealloc() {
        byte[] bytes = new byte[]{ 1, 2, 3, 4 };
        ByteBuffer oldByteBuffer = ByteBuffer.wrap(bytes);
        oldByteBuffer.get();
        oldByteBuffer.get();
        assertEquals(4, oldByteBuffer.capacity());
        assertEquals(2, oldByteBuffer.position());
        ByteBuffer byteBuffer = MemReallocRedirections.memRealloc(oldByteBuffer, 8);
        assertEquals(2, oldByteBuffer.position());
        assertEquals(2, byteBuffer.position());
        assertEquals(8, byteBuffer.capacity());
        assertEquals(1, byteBuffer.get(0));
        assertEquals(2, byteBuffer.get(1));
        assertEquals(3, byteBuffer.get(2));
        assertEquals(4, byteBuffer.get(3));
    }

}
