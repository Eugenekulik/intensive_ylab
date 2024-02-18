package by.eugenekulik.exception;


/**
 * Exception thrown to indicate authentication-related errors.
 *
 * @author Eugene Kulik
 */
public class AuthenticationException extends RuntimeException {


    /**
     * Constructs a new AuthenticationException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the getMessage() method).
     */
    public AuthenticationException(String message) {
        super(message);
    }

}
