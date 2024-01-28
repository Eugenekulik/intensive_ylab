package by.eugenekulik.in.console;

import by.eugenekulik.model.User;


public class Session {

    private static User currentUser;
    private static RequestData requestData;
    private static ResponceData responceData;

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

    public static ResponceData getResponceData() {
        return responceData;
    }

    public static void setResponceData(ResponceData responceData) {
        Session.responceData = responceData;
    }
}
