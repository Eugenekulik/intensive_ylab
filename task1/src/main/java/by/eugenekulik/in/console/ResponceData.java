package by.eugenekulik.in.console;

public class ResponceData {

    private StringBuilder builder = new StringBuilder();


    public void add(String message){
        builder.append("\n" + message);
    }

    @Override
    public String toString(){
        return builder.toString();
    }



}
