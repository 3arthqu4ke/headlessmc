package java.lang.module;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

/**
 * @since 9
 */
@SuppressWarnings({"unused", "RedundantThrows"})
public interface ModuleReader extends Closeable {
    default Optional<ByteBuffer> read(String name) throws IOException {
        throw new IllegalStateException("stub");
    }

    default void release(ByteBuffer bb) {
        throw new IllegalStateException("stub");
    }

}
