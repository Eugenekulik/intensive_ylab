package by.eugenekulik.exception;


/**
 * Exception thrown to indicate validation errors in commands and parameters.
 *
 * @author Eugene Kulik
 */
public class ValidationException extends RuntimeException {

    /**
     * Constructs a new AccessDeniedException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method).
     */
    public ValidationException(String message) {
        super(message);
    }
}
