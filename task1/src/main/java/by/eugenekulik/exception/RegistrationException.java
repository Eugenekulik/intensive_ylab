package by.eugenekulik.exception;


/**
 * Exception thrown to indicate issues related to user registration.
 *
 * @author Eugene Kulik
 */
public class RegistrationException extends RuntimeException {

    /**
     * Constructs a new AccessDeniedException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method).
     */
    public RegistrationException(String message) {
        super(message);
    }
}
