package by.eugenekulik.model;


import java.time.LocalDateTime;
import java.util.UUID;


public record Rec(UUID id, String message, LocalDateTime time) {
    @Override
    public String toString() {
        return "Rec{" +
            "id='" + id + '\'' +
            ", message='" + message + '\'' +
            ", time=" + time +
            '}';
    }
}
