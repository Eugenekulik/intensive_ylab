package by.eugenekulik.exception;

/**
 * {@code DatabaseInterectionException} is a runtime exception class that is used to
 * indicate an exception related to database interactions. It extends the standard
 * {@code RuntimeException} and provides constructors for creating instances with
 * a custom error message and an optional underlying cause.
 *
 * <p>Example usage:
 * <pre>
 *     try {
 *         // Database interaction code
 *     } catch (SQLException e) {
 *         throw new DatabaseInterectionException("Error during database interaction", e);
 *     }
 * </pre>
 *
 * @author Eugene Kulik
 * @see RuntimeException
 */
public class DatabaseInterectionException extends RuntimeException {


    public DatabaseInterectionException(String message) {
        super(message);
    }

    public DatabaseInterectionException(String message, Throwable e) {
        super(message, e);
    }

}
