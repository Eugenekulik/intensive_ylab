package by.eugenekulik.dto;

public record JwtResponseDto(String token){
    @Override
    public String toString() {
        return "JwtResponseDto{" +
            "token='" + token + '\'' +
            '}';
    }
}
