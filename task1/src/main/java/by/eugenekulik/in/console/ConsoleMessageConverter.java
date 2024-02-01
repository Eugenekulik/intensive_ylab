package by.eugenekulik.in.console;

/**
 * The {@code ConsoleMessageConverter} class provides methods for converting messages
 * between the console and request/response data objects.
 */
public class ConsoleMessageConverter {


    /**
     * Reads the input message and converts it into a {@link RequestData} object.
     *
     * @param message The input message to be converted.
     * @return The {@code RequestData} object representing the input message.
     */
    public RequestData read(String message) {
        String[] words = message.split(" ");
        RequestData requestData = new RequestData();
        requestData.setParam("command", words[0]);
        for (int i = 1; i < words.length; i++) {
            String[] param = words[i].split("=");
            requestData.setParam(param[0], param[1]);
        }
        return requestData;
    }


    /**
     * Writes the provided {@link ResponseData} object into a formatted string for output to the console.
     *
     * @param responseData The {@code ResponseData} object to be converted.
     * @return The formatted string representing the response data.
     */
    public String write(ResponseData responseData) {
        responseData.add(TextColor.ANSI_BLUE.changeColor(
            "###################################################################################"));
        return responseData.toString();
    }

}
