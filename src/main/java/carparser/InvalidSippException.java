package carparser;

// InvalidSippException: thrown if sipp format is not correct

public class InvalidSippException extends Exception {
    public InvalidSippException(String message, Throwable cause) {
        super(message, cause);
    }
}
