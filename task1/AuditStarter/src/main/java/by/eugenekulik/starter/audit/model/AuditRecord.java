package by.eugenekulik.starter.audit.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditRecord {

    private UUID id;
    private String message;
    private LocalDateTime time;

    @Override
    public String toString() {
        return
            "id: " + id +
            ", message: '" + message + '\'' +
            ", time: " + time;
    }
}
