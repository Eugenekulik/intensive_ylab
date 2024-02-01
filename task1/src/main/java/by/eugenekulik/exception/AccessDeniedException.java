package by.eugenekulik.exception;


/**
 * Exception thrown to indicate that access to a command is denied.
 *
 * @author Eugene Kulik
 */
public class AccessDeniedException extends RuntimeException {

    /**
     * Constructs a new AccessDeniedException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method).
     */
    public AccessDeniedException(String message) {
        super(message);
    }
}
