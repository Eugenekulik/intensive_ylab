package by.eugenekulik.in.console;

import java.util.Scanner;

public class View {

    private Scanner scanner = new Scanner(System.in);

    public String read(){
        return scanner.nextLine();
    }



    public void write(String message){
        System.out.println(message);
    }

}
