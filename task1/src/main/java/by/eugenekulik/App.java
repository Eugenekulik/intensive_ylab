package by.eugenekulik;

import by.eugenekulik.config.Configuration;

public class App {
    public static void main(String[] args) {
        Configuration configuration = new Configuration("application.properties");
        configuration.controller().start();
    }
}
