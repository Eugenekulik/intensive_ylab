package by.eugenekulik.model;


import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


public record Rec(String id, String message, LocalDateTime time) {
    @Override
    public String toString() {
        return "Rec{" +
            "id='" + id + '\'' +
            ", message='" + message + '\'' +
            ", time=" + time +
            '}';
    }
}
