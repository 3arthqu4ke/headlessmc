package me.earth.headlessmc.api.process;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("resource")
public class WritableInputStreamTest {
    @Test
    public void testWritableInputStream() throws IOException {
        WritableInputStream writableInputStream = new WritableInputStream();

        assertEquals(-1, writableInputStream.read());

        writableInputStream.getPrintStream().print('A');
        writableInputStream.getPrintStream().print('B');
        writableInputStream.getPrintStream().print('C');

        assertEquals('A', writableInputStream.read());
        assertEquals('B', writableInputStream.read());
        assertEquals('C', writableInputStream.read());

        assertEquals(-1, writableInputStream.read());
    }

    @Test
    public void testOutputStream() throws IOException {
        WritableInputStream writableInputStream = new WritableInputStream();

        writableInputStream.getOutputStream().write('X');
        writableInputStream.getOutputStream().write('Y');
        writableInputStream.getOutputStream().write('Z');

        assertEquals('X', writableInputStream.read());
        assertEquals('Y', writableInputStream.read());
        assertEquals('Z', writableInputStream.read());

        assertEquals(-1, writableInputStream.read());
    }

    @Test
    public void testReadAfterClose() throws IOException {
        WritableInputStream writableInputStream = new WritableInputStream();

        writableInputStream.getPrintStream().print('1');
        writableInputStream.getPrintStream().print('2');
        writableInputStream.getPrintStream().print('3');

        writableInputStream.getPrintStream().close();

        assertEquals('1', writableInputStream.read());
        assertEquals('2', writableInputStream.read());
        assertEquals('3', writableInputStream.read());

        assertEquals(-1, writableInputStream.read());
    }
}
