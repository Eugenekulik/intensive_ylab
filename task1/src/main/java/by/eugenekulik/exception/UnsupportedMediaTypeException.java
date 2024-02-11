package by.eugenekulik.exception;

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
