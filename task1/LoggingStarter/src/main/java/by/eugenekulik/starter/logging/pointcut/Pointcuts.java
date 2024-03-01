package by.eugenekulik.starter.logging.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("@annotation(by.eugenekulik.starter.logging.annotation.Loggable)")
    public void callLoggableMethod() {
    }


    @Pointcut("@annotation(by.eugenekulik.starter.logging.annotation.Timed)")
    public void callTimedMethod() {
    }


}
