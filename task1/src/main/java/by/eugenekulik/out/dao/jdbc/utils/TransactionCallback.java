package by.eugenekulik.out.dao.jdbc.utils;



@FunctionalInterface
public interface TransactionCallback<T> {

    T execute();

}
