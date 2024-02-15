package by.eugenekulik.exception;

/**
 * {@code UnsupportedMediaTypeException} is a runtime exception class that is used to
 * indicate an unsupported media type during a specific operation. It extends the standard
 * {@code RuntimeException} and provides constructors for creating instances with
 * a custom error message, no-argument constructor, and an optional underlying cause.
 *
 * <p>This exception is typically thrown when a certain media type is not supported by a
 * particular operation or component.
 *
 * @author Eugene Kulik
 * @see RuntimeException
 */
public class UnsupportedMediaTypeException extends RuntimeException {

    public UnsupportedMediaTypeException(String message) {
        super(message);
    }

    public UnsupportedMediaTypeException() {
        super();
    }

    public UnsupportedMediaTypeException(String message, Throwable e) {
        super(message, e);
    }


}
