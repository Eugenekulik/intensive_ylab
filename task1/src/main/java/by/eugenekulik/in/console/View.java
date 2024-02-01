package by.eugenekulik.in.console;

import java.util.Scanner;

/**
 * The {@code View} class provides methods for reading input and writing output to the console.
 */
public class View {

    /**
     * A {@code Scanner} object for reading input from the console.
     */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a line of input from the console.
     *
     * @return The input read from the console.
     */
    public String read() {
        return scanner.nextLine();
    }

    /**
     * Writes a message to the console.
     *
     * @param message The message to be written to the console.
     */
    public void write(String message) {
        System.out.println(message);
    }
}
