package nl.avisi.exception;

public class InvalidCredentialsException extends Exception {
    private String errorMessage;

    public InvalidCredentialsException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return this.errorMessage;
    }
}
