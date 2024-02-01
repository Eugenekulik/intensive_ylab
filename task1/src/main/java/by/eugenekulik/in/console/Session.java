package by.eugenekulik.in.console;

import by.eugenekulik.model.User;


public class Session {

    private static User currentUser;
    private static RequestData requestData;
    private static ResponseData responseData;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Session.currentUser = currentUser;
    }

    public static RequestData getRequestData() {
        return requestData;
    }

    public static void setRequestData(RequestData requestData) {
        Session.requestData = requestData;
    }

    public static ResponseData getResponceData() {
        return responseData;
    }

    public static void setResponceData(ResponseData responseData) {
        Session.responseData = responseData;
    }
}
