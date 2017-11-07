package carparser;

// InvalidSortParameterException: thrown if query parameter for sorting is invalid

public class InvalidSortParameterException extends Exception {
    public InvalidSortParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
