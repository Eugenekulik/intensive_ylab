package by.eugenekulik.in.console;

public class ResponceData {

    private final StringBuilder builder = new StringBuilder();


    public void add(String message){
        builder.append("\n").append(message);
    }

    @Override
    public String toString(){
        return builder.toString();
    }



}
