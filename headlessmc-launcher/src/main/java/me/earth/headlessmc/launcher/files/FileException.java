package me.earth.headlessmc.launcher.files;

/**
 * Thrown by the {@link FileManager}.
 */
// TODO: @StandardException
public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

}
