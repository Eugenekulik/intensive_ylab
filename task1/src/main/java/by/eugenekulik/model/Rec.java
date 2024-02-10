package by.eugenekulik.model;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public record Rec(String message, LocalDateTime time, String username) {}
