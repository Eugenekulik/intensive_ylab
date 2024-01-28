package by.eugenekulik.in.console;

public class ConsoleMessageConverter {



    public RequestData read(String message){
        String[] words = message.split(" ");
        RequestData requestData = new RequestData();
        requestData.getParams().put("command", words[0]);
        for(int i = 1; i < words.length; i++){
            String[] param = words[i].split("=");
            requestData.getParams().put(param[0], param[1]);
        }
        return requestData;
    }



    public String write(ResponceData responceData){
        return responceData.toString();
    }



}
