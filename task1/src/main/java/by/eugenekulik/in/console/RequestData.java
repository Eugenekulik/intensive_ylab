package by.eugenekulik.in.console;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class RequestData {


    private HashMap<String, String> params;
    private HashMap<String, Object> attributes;


    public RequestData(){
        params = new HashMap<>();
        attributes = new HashMap<>();
    }

}
