package by.eugenekulik.in.console;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The {@code RequestData} class represents the data associated with a request.
 * It contains parameters and attributes that are essential for processing a request.
 */
public class RequestData {
    /**
     * A map to store request parameters.
     */
    private HashMap<String, String> params;

    /**
     * A map to store additional attributes related to the request.
     */
    private HashMap<String, Object> attributes;

    /**
     * Constructs a new {@code RequestData} object with empty parameter and attribute maps.
     */
    public RequestData() {
        params = new HashMap<>();
        attributes = new HashMap<>();
    }

    public String getParam(String name) {
        return params.get(name);
    }

    public void setParam(String name, String param) {
        params.put(name, param);
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public void setAttribute(String name, Object attribute) {
        attributes.put(name, attribute);
    }

    public Stream<Map.Entry<String, String>> getParamsStream() {
        return params.entrySet().stream();
    }
}
