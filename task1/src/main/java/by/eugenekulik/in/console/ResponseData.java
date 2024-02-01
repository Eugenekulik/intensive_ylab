package by.eugenekulik.in.console;

/**
 * The {@code ResponseData} class represents the data to be sent as a response.
 * It provides methods to add messages and convert the response data to a string for output.
 */
public class ResponseData {

    /**
     * A {@code StringBuilder} to accumulate messages for the response.
     */
    private final StringBuilder builder = new StringBuilder();

    /**
     * Adds a message to the response data.
     *
     * @param message The message to be added.
     */
    public void add(String message) {
        builder.append("\n").append(message);
    }

    /**
     * Converts the response data to a string for output.
     *
     * @return The formatted string representing the response data.
     */
    @Override
    public String toString() {
        return builder.toString();
    }
}