package by.eugenekulik.exception;

public class DatabaseInterectionException extends RuntimeException {


    public DatabaseInterectionException(String message) {
        super(message);
    }

    public DatabaseInterectionException(String message, Throwable e) {
        super(message, e);
    }

}
