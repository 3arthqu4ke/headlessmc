package paulscode.sound;

import lombok.Getter;

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
