package by.eugenekulik.utils;

import by.eugenekulik.exception.UnsupportedMediaTypeException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@Slf4j
public class Converter {

    private final ObjectMapper objectMapper;

    public Converter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T getRequestBody(HttpServletRequest request, Class<T> valueType) {
        if (!request.getContentType().equals("application/json")) {
            throw new UnsupportedMediaTypeException("not supported content type");
        }
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
            return objectMapper.readValue(requestBody.toString(), valueType);
        } catch (IOException e) {
            throw new UnsupportedMediaTypeException("");
        }
    }

    public String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); //TODO
        }
    }

    public int getInteger(HttpServletRequest req, String paramName) {
        return Optional.ofNullable(req.getParameter(paramName)).map(p -> {
            try {
                return Integer.parseInt(p);
            } catch (NumberFormatException e) {
                log.info("wrong value of {}: {}", paramName, p);
            }
            return 0;
        }).orElse(0);
    }

    public long getLong(HttpServletRequest req, String paramName) {
        return Optional.ofNullable(req.getParameter(paramName)).map(p -> {
            try {
                return Long.parseLong(p);
            } catch (NumberFormatException e) {
                log.info("wrong value of {}: {}", paramName, p);
            }
            return 0L;
        }).orElse(0L);
    }


}
