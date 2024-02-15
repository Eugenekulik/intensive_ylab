package by.eugenekulik.exception;

public class TransactionException extends RuntimeException{

    public TransactionException(String message, Throwable e){
        super(message, e);
    }
    public TransactionException(String message){
        super(message);
    }
    public TransactionException(){
        super();
    }
}
