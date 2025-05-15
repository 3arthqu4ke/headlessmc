package paulscode.sound;

import lombok.Getter;
import io.github.headlesshq.headlessmc.launcher.instrumentation.paulscode.PaulscodeTransformerTest;

/**
 * This is a mock of the paulscode Library class which we instrument.
 * {@link PaulscodeTransformerTest}
 */
@SuppressWarnings("unused")
public class Library {
    @Getter
    private String message;

    public void message(String message) {
        this.message = message;
    }

    public void importantMessage(String message) {
        this.message = message;
    }

    public void errorCheck(boolean error, String message) {
        if (error) {
            this.message = message;
        }
    }

    public void errorMessage(String message) {
        this.message = message;
    }

    public void printStackTrace(Exception exception) {
        this.message = exception.getMessage();
    }

}
