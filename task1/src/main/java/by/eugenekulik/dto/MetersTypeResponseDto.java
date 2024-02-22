package by.eugenekulik.dto;

public record MetersTypeResponseDto(
    Long id,
    String name
) {
    @Override
    public String toString() {
        return "MetersTypeResponseDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
