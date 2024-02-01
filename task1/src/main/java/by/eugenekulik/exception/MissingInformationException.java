package by.eugenekulik.exception;


/**
 * Exception thrown to indicate that essential information about command is missing.
 *
 * @author Eugene Kulik
 */
public class MissingInformationException extends RuntimeException {

    /**
     * Constructs a new AccessDeniedException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method).
     */
    public MissingInformationException(String message) {
        super(message);
    }
}
