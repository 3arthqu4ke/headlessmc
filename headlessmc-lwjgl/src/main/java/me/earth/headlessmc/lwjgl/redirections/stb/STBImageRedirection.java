package me.earth.headlessmc.lwjgl.redirections.stb;

import me.earth.headlessmc.lwjgl.api.Redirection;
import me.earth.headlessmc.lwjgl.util.ByteBufferInputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public enum STBImageRedirection implements Redirection {
    INSTANCE;

    private static final BufferedImage DUMMY = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    @Override
    public Object invoke(Object obj, String desc, Class<?> type, Object... args)
        throws Throwable {
        ByteBuffer buffer = (ByteBuffer) args[0];
        IntBuffer x = (IntBuffer) args[1];
        IntBuffer y = (IntBuffer) args[2];
        IntBuffer channels_in_file = (IntBuffer) args[3];
        int desired_channels = (int) args[4];

        ByteBuffer result = ByteBuffer.wrap(
            new byte[x.get(x.position()) * y.get(y.position())
                * (desired_channels != 0
                ? desired_channels
                : channels_in_file.get(channels_in_file.position()))]);

        BufferedImage image = readImage(buffer);
        x.put(0, image.getWidth());
        y.put(0, image.getHeight());
        // TODO: check discrepancies between desired_channels and actual
        channels_in_file.put(0, desired_channels);
        return result;
    }

    private BufferedImage readImage(ByteBuffer buffer) {
        int position = buffer.position();
        BufferedImage image = null;
        try {
            image = ImageIO.read(new ByteBufferInputStream(buffer));
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        buffer.position(position);
        return image == null ? DUMMY : image;
    }

}
